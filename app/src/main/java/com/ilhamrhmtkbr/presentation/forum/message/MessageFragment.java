package com.ilhamrhmtkbr.presentation.forum.message;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.databinding.FragmentForumChatBinding;
import com.ilhamrhmtkbr.data.remote.dto.response.ForumGroupResponse;
import com.ilhamrhmtkbr.domain.model.forum.Chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MessageFragment extends Fragment implements
        MessageWebSocket.ForumWebSocketListener,
        MessageAdapter.OnLoadMoreListener {

    private static final String TAG = "MessageFragment";
    private static final String ARG_COURSE_ID = "course_id";
    private static final String ARG_COURSE_TITLE = "course_title";
    private static final int SCROLL_TIMEOUT = 1000;

    private FragmentForumChatBinding binding;
    private MessageAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    private List<Chat> messages = new ArrayList<>();
    private Set<String> messageIds = new HashSet<>();

    @Inject
    MessageWebSocket webSocketManager;

    @Inject
    AuthStateManager authStateManager;

    private MessageViewModel messageViewModel;

    private String currentCourseId;
    private String currentCourseTitle;
    private String currentUserId;

    private boolean hasMoreMessages = true;
    private boolean isLoadingMore = false;

    // Auto scroll management
    private boolean shouldAutoScroll = true;
    private boolean isUserScrolling = false;
    private Handler scrollHandler = new Handler(Looper.getMainLooper());
    private Runnable scrollTimeoutRunnable;

    // Track loading state
    private boolean isInitialLoad = true;
    private int firstVisiblePositionBeforeLoad = -1;
    private int offsetBeforeLoad = 0;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        if (getArguments() != null) {
            currentCourseId = getArguments().getString(ARG_COURSE_ID);
            currentCourseTitle = getArguments().getString(ARG_COURSE_TITLE);
        }

        currentUserId = authStateManager.getCurrentUsername() != null
                ? authStateManager.getCurrentUsername() : "";

        webSocketManager.setListener(this);
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater,
                             ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentForumChatBinding.inflate(layoutInflater, viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        setupRecyclerView();
        setupInputControls();
        observeMessages();

        if (currentCourseId != null && !currentCourseId.isEmpty()) {
            loadInitialMessages();
            connectWebSocket();
        }
    }

    private void setupRecyclerView() {
        layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setReverseLayout(false);
        layoutManager.setStackFromEnd(true);
        binding.waChatContent.setLayoutManager(layoutManager);

        chatAdapter = new MessageAdapter(messages, currentUserId, this);
        binding.waChatContent.setAdapter(chatAdapter);

        binding.waChatContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                handleScrollEvent();
            }
        });
    }

    private void setupInputControls() {
        binding.waSend.setOnClickListener(v -> sendMessage());

        binding.waChatInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                            && !event.isShiftPressed())) {
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private void loadInitialMessages() {
        if (currentCourseId == null) return;

        isInitialLoad = true;
        hasMoreMessages = true;
        isLoadingMore = false;

        messageViewModel.loadMessages(currentCourseId, "");
    }

    private void connectWebSocket() {
        if (currentCourseId != null && !currentCourseId.isEmpty()) {
            Log.d(TAG, "Connecting WebSocket for course: " + currentCourseId);
            webSocketManager.connect(currentCourseId);
        }
    }

    private void observeMessages() {
        messageViewModel.getChatMessage().observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;

            switch (resource.getStatus()) {
                case SUCCESS:
                    handleMessagesLoaded(resource.getData());
                    break;

                case ERROR:
                    showError(resource.getMessage());
                    isLoadingMore = false;
                    chatAdapter.setHasMore(hasMoreMessages);
                    break;

                case LOADING:
                    // Already handled by isLoadingMore flag
                    break;
            }
        });
    }

    private void handleMessagesLoaded(List<Chat> newMessages) {
        if (newMessages == null) {
            isLoadingMore = false;
            return;
        }

        if (newMessages.isEmpty()) {
            hasMoreMessages = false;
            isLoadingMore = false;
            chatAdapter.setHasMore(false);
            return;
        }

        boolean wasInitialLoad = isInitialLoad;
        isInitialLoad = false;

        // Process and deduplicate messages
        List<Chat> processedMessages = new ArrayList<>();
        for (Chat msg : newMessages) {
            Chat normalized = normalizeMessageTimestamp(msg);
            String msgId = generateMessageId(normalized);

            if (!messageIds.contains(msgId)) {
                messageIds.add(msgId);
                processedMessages.add(normalized);
            } else {
                Log.w(TAG, "⚠️ Skipping duplicate message: " + msgId);
            }
        }

        if (processedMessages.isEmpty()) {
            Log.d(TAG, "No new unique messages to add");
            isLoadingMore = false;
            hasMoreMessages = false;
            chatAdapter.setHasMore(false);
            return;
        }

        if (wasInitialLoad) {
            // Initial load: replace all messages
            messages.clear();
            messages.addAll(processedMessages);
            chatAdapter.updateMessages(messages);

            Log.d(TAG, "✅ Initial load: " + messages.size() + " messages");

            // Scroll to bottom after initial load
            scrollHandler.postDelayed(() -> scrollToBottom(true), 100);
            shouldAutoScroll = true;
            isUserScrolling = false;
        } else {
            // Load more: insert at beginning and restore scroll position
            Collections.reverse(processedMessages);

            messages.addAll(0, processedMessages);
            chatAdapter.updateMessages(messages);

            Log.d(TAG, "✅ Loaded " + processedMessages.size() + " older messages (Total: " + messages.size() + ")");

            // Restore scroll position
            int addedCount = processedMessages.size();
            scrollHandler.post(() -> {
                int newPosition = firstVisiblePositionBeforeLoad + addedCount;
                if (hasMoreMessages) {
                    newPosition += 1; // Account for load more item
                }
                layoutManager.scrollToPositionWithOffset(newPosition, offsetBeforeLoad);
            });
        }

        // Update hasMore status
        hasMoreMessages = newMessages.size() >= 10;
        isLoadingMore = false;
        chatAdapter.setHasMore(hasMoreMessages);
    }

    private void sendMessage() {
        String messageText = binding.waChatInput.getText().toString().trim();
        if (messageText.isEmpty()) return;

        messageViewModel.sendMessage(messageText, currentCourseId);
        binding.waChatInput.setText("");

        // Message will be added via WebSocket callback
    }

    // ========== WebSocket Callbacks ==========

    @Override
    public void onMessageReceived(Chat response) {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            Chat normalized = normalizeMessageTimestamp(response);

            // Check for duplicate
            if (isMessageDuplicate(normalized)) {
                Log.w(TAG, "⚠️ Duplicate WebSocket message - SKIPPING");
                return;
            }

            // Add message safely
            addMessageSafely(normalized);
            chatAdapter.updateMessages(messages);

            Log.d(TAG, "✅ New WebSocket message (Total: " + messages.size() + ")");

            // Auto scroll if user is at bottom
            if (shouldAutoScroll && !isUserScrolling) {
                scrollHandler.postDelayed(() -> scrollToBottom(false), 50);
            }
        });
    }

    @Override
    public void onConnectionError(String error) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            Log.e(TAG, "❌ WebSocket error: " + error);
            showError("Connection error: " + error);
        });
    }

    @Override
    public void onConnected() {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> Log.d(TAG, "✅ WebSocket connected"));
    }

    @Override
    public void onDisconnected() {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> Log.d(TAG, "⚠️ WebSocket disconnected"));
    }

    // ========== Load More Implementation ==========

    @Override
    public void onLoadMore() {
        if (isLoadingMore || !hasMoreMessages || messages.isEmpty()) {
            Log.d(TAG, "Load more cancelled - loading: " + isLoadingMore +
                    ", hasMore: " + hasMoreMessages + ", empty: " + messages.isEmpty());
            return;
        }

        // Save current scroll position
        firstVisiblePositionBeforeLoad = layoutManager.findFirstVisibleItemPosition();
        View firstView = layoutManager.findViewByPosition(firstVisiblePositionBeforeLoad);
        offsetBeforeLoad = (firstView == null) ? 0 : firstView.getTop();

        // Get oldest message timestamp
        String oldestTimestamp = messages.get(0).getCreatedAt();

        Log.d(TAG, "📥 Loading messages before: " + oldestTimestamp);

        isLoadingMore = true;
        chatAdapter.setHasMore(hasMoreMessages); // Trigger loading UI

        // Load older messages - result will be handled in observeMessages
        messageViewModel.loadMessages(currentCourseId, oldestTimestamp);
    }

    @Override
    public boolean isLoading() {
        return isLoadingMore;
    }

    @Override
    public boolean hasMore() {
        return hasMoreMessages;
    }

    // ========== Helper Methods ==========

    private void handleScrollEvent() {
        if (layoutManager == null) return;

        shouldAutoScroll = isNearBottom();

        // Reset scrolling state after delay
        if (scrollTimeoutRunnable != null) {
            scrollHandler.removeCallbacks(scrollTimeoutRunnable);
        }

        isUserScrolling = true;
        scrollTimeoutRunnable = () -> isUserScrolling = false;
        scrollHandler.postDelayed(scrollTimeoutRunnable, SCROLL_TIMEOUT);
    }

    private boolean isNearBottom() {
        if (layoutManager == null) return true;

        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
        int totalItems = layoutManager.getItemCount();

        return totalItems - lastVisiblePosition <= 3;
    }

    private void scrollToBottom(boolean force) {
        if (force || shouldAutoScroll) {
            if (chatAdapter.getItemCount() > 0) {
                scrollHandler.post(() -> {
                    int lastPosition = chatAdapter.getItemCount() - 1;
                    binding.waChatContent.smoothScrollToPosition(lastPosition);
                });
            }
        }
    }

    private String generateMessageId(Chat message) {
        if (message == null) return "";

        String username = message.getUsername() != null ? message.getUsername() : "";
        String timestamp = message.getCreatedAt() != null ? message.getCreatedAt() : "";
        String messageText = message.getMessage() != null ? message.getMessage() : "";

        return username + "_" + timestamp + "_" + messageText.hashCode();
    }

    private boolean isMessageDuplicate(Chat message) {
        String messageId = generateMessageId(message);
        return messageIds.contains(messageId);
    }

    private void addMessageSafely(Chat message) {
        if (message == null) return;

        String messageId = generateMessageId(message);

        if (messageIds.contains(messageId)) {
            Log.w(TAG, "⚠️ Duplicate message - SKIPPING: " + messageId);
            return;
        }

        messageIds.add(messageId);
        messages.add(message);

        Log.d(TAG, "✅ Message added: " + messageId);
    }

    private Chat normalizeMessageTimestamp(Chat message) {
        if (message == null || message.getCreatedAt() == null || message.getCreatedAt().isEmpty()) {
            return message;
        }

        try {
            // Already in ISO format
            if (message.getCreatedAt().contains("T") && message.getCreatedAt().contains("Z")) {
                return message;
            }

            // Convert from "yyyy-MM-dd HH:mm:ss" to ISO format
            if (message.getCreatedAt().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date date = oldFormat.parse(message.getCreatedAt());

                if (date != null) {
                    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                    isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    message.setCreatedAt(isoFormat.format(date));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ Error normalizing timestamp: " + message.getCreatedAt(), e);
        }

        return message;
    }

    private void showError(String message) {
        if (getContext() == null) return;
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    // ========== Public Methods ==========

    public void setCourse(ForumGroupResponse group) {
        disconnectWebSocket();

        currentCourseId = group.id;
        currentCourseTitle = group.title;

        // Clear all data
        messages.clear();
        messageIds.clear();
        chatAdapter.updateMessages(messages);

        hasMoreMessages = true;
        isLoadingMore = false;
        shouldAutoScroll = true;
        isUserScrolling = false;
        isInitialLoad = true;

        loadInitialMessages();
        connectWebSocket();
    }

    private void disconnectWebSocket() {
        Log.d(TAG, "🔌 Disconnecting WebSocket");
        webSocketManager.disconnect();
    }

    // ========== Lifecycle Methods ==========

    @Override
    public void onResume() {
        super.onResume();
        if (currentCourseId != null && !webSocketManager.isConnected()) {
            connectWebSocket();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disconnectWebSocket();

        if (scrollHandler != null && scrollTimeoutRunnable != null) {
            scrollHandler.removeCallbacks(scrollTimeoutRunnable);
        }

        messageIds.clear();
        binding = null;
    }
}