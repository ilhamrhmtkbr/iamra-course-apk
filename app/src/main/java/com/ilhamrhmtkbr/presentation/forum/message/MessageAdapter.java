package com.ilhamrhmtkbr.presentation.forum.message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.domain.model.forum.Chat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MessageAdapter";

    private static final int TYPE_LOAD_MORE = 0;
    private static final int TYPE_ME = 1;
    private static final int TYPE_OTHER = 2;

    private List<Chat> chatList;
    private String currentUserId;
    private OnLoadMoreListener loadMoreListener;

    public interface OnLoadMoreListener {
        void onLoadMore();
        boolean isLoading();
        boolean hasMore();
    }

    public MessageAdapter(List<Chat> chatList, String currentUserId, OnLoadMoreListener listener) {
        this.chatList = chatList;
        this.currentUserId = currentUserId;
        this.loadMoreListener = listener;
        setHasStableIds(false);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOAD_MORE:
                View loadMoreView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum_load_more_message, parent, false);
                return new LoadMoreViewHolder(loadMoreView);
            case TYPE_ME:
                View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum_chat_me, parent, false);
                return new ChatMeViewHolder(myView);
            default:
                View otherView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum_chat_others, parent, false);
                return new ChatOtherViewHolder(otherView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        try {
            switch (viewType) {
                case TYPE_LOAD_MORE:
                    LoadMoreViewHolder loadMoreHolder = (LoadMoreViewHolder) holder;
                    loadMoreHolder.bind(loadMoreListener);
                    break;

                case TYPE_ME:
                case TYPE_OTHER:
                    int chatPosition = hasLoadMore() ? position - 1 : position;

                    if (chatPosition >= 0 && chatPosition < chatList.size()) {
                        Chat chat = chatList.get(chatPosition);

                        if (viewType == TYPE_ME) {
                            ChatMeViewHolder meHolder = (ChatMeViewHolder) holder;
                            meHolder.bind(chat);
                        } else {
                            ChatOtherViewHolder otherHolder = (ChatOtherViewHolder) holder;
                            otherHolder.bind(chat);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error binding view holder at position " + position, e);
        }
    }

    @Override
    public int getItemCount() {
        int baseCount = chatList != null ? chatList.size() : 0;
        int totalCount = hasLoadMore() ? baseCount + 1 : baseCount;

        Log.d(TAG, "getItemCount: messages=" + baseCount + ", hasLoadMore=" + hasLoadMore() + ", total=" + totalCount);
        return totalCount;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: position=" + position + ", hasLoadMore=" + hasLoadMore());

        if (position == 0 && hasLoadMore()) {
            Log.d(TAG, "Returning TYPE_LOAD_MORE for position " + position);
            return TYPE_LOAD_MORE;
        }

        int chatPosition = hasLoadMore() ? position - 1 : position;

        Log.d(TAG, "Chat position for " + position + " = " + chatPosition + " (chatList size: " + (chatList != null ? chatList.size() : 0) + ")");

        if (chatPosition >= 0 && chatPosition < (chatList != null ? chatList.size() : 0)) {
            Chat chat = chatList.get(chatPosition);
            if (chat.getUsername() != null && chat.getUsername().equals(currentUserId)) {
                return TYPE_ME;
            } else {
                return TYPE_OTHER;
            }
        }

        Log.w(TAG, "Returning TYPE_OTHER as fallback for position " + position);
        return TYPE_OTHER;
    }

    private boolean hasLoadMore() {
        return loadMoreListener != null && loadMoreListener.hasMore();
    }

    // ========== VIEW HOLDERS ==========

    // Load More ViewHolder
    // Di LoadMoreViewHolder, perbaiki bind method:

    static class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        TextView loadMoreText;
        ProgressBar loadMoreProgress;

        public LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);
            loadMoreText = itemView.findViewById(R.id.load_more_text);
            loadMoreProgress = itemView.findViewById(R.id.load_more_progress);
        }

        public void bind(OnLoadMoreListener listener) {
            if (listener == null) return;

            boolean isLoading = listener.isLoading();
            boolean hasMore = listener.hasMore();

            // Update UI based on state
            if (isLoading) {
                loadMoreText.setVisibility(View.GONE);
                loadMoreProgress.setVisibility(View.VISIBLE);
                itemView.setEnabled(false);
                itemView.setAlpha(0.6f);
                itemView.setOnClickListener(null); // Remove listener saat loading
            } else if (hasMore) {
                loadMoreText.setVisibility(View.VISIBLE);
                loadMoreProgress.setVisibility(View.GONE);
                loadMoreText.setText("Load older messages");
                itemView.setEnabled(true);
                itemView.setAlpha(1.0f);
                itemView.setOnClickListener(v -> listener.onLoadMore());
            } else {
                // No more messages
                loadMoreText.setVisibility(View.VISIBLE);
                loadMoreProgress.setVisibility(View.GONE);
                loadMoreText.setText("No more messages");
                itemView.setEnabled(false);
                itemView.setAlpha(0.6f);
                itemView.setOnClickListener(null);
            }
        }
    }

    // Other user's message ViewHolder
    static class ChatOtherViewHolder extends RecyclerView.ViewHolder {
        TextView chatId, chatMessage, chatAt, chatName, chatRole;

        public ChatOtherViewHolder(@NonNull View itemView) {
            super(itemView);
            chatId = itemView.findViewById(R.id.chat_other_id);
            chatMessage = itemView.findViewById(R.id.chat_other_message);
            chatAt = itemView.findViewById(R.id.chat_other_at);
        }

        public void bind(Chat chat) {
            if (chatId != null) {
                chatId.setText(chat.getUsername() != null ? chat.getUsername() : "");
            }

            if (chatName != null && chat.getName() != null) {
                chatName.setText(chat.getName());
            }

            if (chatRole != null && chat.getRole() != null) {
                chatRole.setText(chat.getRole());
            }

            chatMessage.setText(chat.getMessage() != null ? chat.getMessage() : "");
            chatAt.setText(formatMessageTime(chat.getCreatedAt()));
        }
    }

    // Current user's message ViewHolder
    static class ChatMeViewHolder extends RecyclerView.ViewHolder {
        TextView chatMessage, chatAt;

        public ChatMeViewHolder(@NonNull View itemView) {
            super(itemView);
            chatMessage = itemView.findViewById(R.id.chat_me_message);
            chatAt = itemView.findViewById(R.id.chat_me_at);
        }

        public void bind(Chat chat) {
            chatMessage.setText(chat.getMessage() != null ? chat.getMessage() : "");
            chatAt.setText(formatMessageTime(chat.getCreatedAt()));
        }
    }

    // ========== TIME FORMATTING UTILITIES ==========

    /**
     * Format message timestamp based on how recent it is
     * - Today: show only time (HH:mm)
     * - This year: show date and time (dd MMM, HH:mm)
     * - Older: show full date and time (dd MMM yyyy, HH:mm)
     */
    private static String formatMessageTime(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return "";
        }

        try {
            // Parse ISO 8601 timestamp dari backend
            Date messageDate = parseISOTimestamp(timestamp);
            if (messageDate == null) {
                Log.w(TAG, "Failed to parse timestamp: " + timestamp);
                return timestamp; // Fallback ke raw timestamp
            }

            Calendar messageCal = Calendar.getInstance();
            messageCal.setTime(messageDate);

            Calendar nowCal = Calendar.getInstance();

            // Check if same day
            if (isSameDay(messageCal, nowCal)) {
                // Today: hanya tampilkan jam
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                return timeFormat.format(messageDate);
            }

            // Check if same year
            if (messageCal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR)) {
                // This year: tanggal + jam (contoh: "19 Dec, 10:30")
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault());
                return dateTimeFormat.format(messageDate);
            }

            // Different year: full date + jam (contoh: "19 Dec 2024, 10:30")
            SimpleDateFormat fullDateTimeFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
            return fullDateTimeFormat.format(messageDate);

        } catch (Exception e) {
            Log.e(TAG, "Error formatting time: " + timestamp, e);
            return timestamp; // Fallback
        }
    }

    /**
     * Parse ISO 8601 timestamp dari backend
     * Contoh format: "2025-12-19T10:58:52.953000Z"
     */
    private static Date parseISOTimestamp(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return null;
        }

        try {
            // Format ISO 8601 dengan microseconds
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return isoFormat.parse(timestamp);
        } catch (ParseException e) {
            // Try without microseconds
            try {
                SimpleDateFormat isoFormatSimple = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
                isoFormatSimple.setTimeZone(TimeZone.getTimeZone("UTC"));
                return isoFormatSimple.parse(timestamp);
            } catch (ParseException ex) {
                Log.e(TAG, "Failed to parse ISO timestamp: " + timestamp, ex);
                return null;
            }
        }
    }

    /**
     * Check if two calendars are on the same day
     */
    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    // ========== PUBLIC METHODS ==========

    public void updateMessages(List<Chat> newMessages) {
        Log.d(TAG, "=== ADAPTER UPDATE ===");
        Log.d(TAG, "Old chatList size: " + (this.chatList != null ? this.chatList.size() : 0));
        Log.d(TAG, "New messages size: " + (newMessages != null ? newMessages.size() : 0));

        this.chatList = newMessages;

        Log.d(TAG, "After assignment chatList size: " + (this.chatList != null ? this.chatList.size() : 0));
        Log.d(TAG, "======================");

        notifyDataSetChanged();
    }

    public void setHasMore(boolean hasMore) {
        notifyDataSetChanged();
        Log.d(TAG, "Updated hasMore status to: " + hasMore);
    }
}