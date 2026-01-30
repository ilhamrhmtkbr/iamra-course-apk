package com.ilhamrhmtkbr.presentation.forum.message;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.core.network.CookieManager;
import com.ilhamrhmtkbr.domain.model.forum.Chat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class MessageWebSocket {
    private static final String TAG = "ForumWebSocket";
    private static MessageWebSocket instance;

    private WebSocket webSocket;
    private final OkHttpClient httpClient;
    private String currentCourseId;
    private String socketId;
    private boolean isConnected = false;
    private ForumWebSocketListener listener;

    // Retry mechanism
    private int retryCount = 0;
    private static final int MAX_RETRIES = 3;
    private static final int RECONNECT_DELAY = 3000;

    public interface ForumWebSocketListener {
        void onMessageReceived(Chat message);
        void onConnectionError(String error);
        void onConnected();
        void onDisconnected();
    }

    @Inject
    public MessageWebSocket(CookieManager cookieManager) {
        this.httpClient = new OkHttpClient.Builder()
                .cookieJar(cookieManager.getCookieJar())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public void setListener(ForumWebSocketListener listener) {
        this.listener = listener;
    }

    public void connect(String courseId) {
        if (courseId == null || courseId.isEmpty()) {
            Log.e(TAG, "Course ID is required");
            return;
        }

        // Disconnect existing connection jika ada
        disconnect();

        this.currentCourseId = courseId;
        this.retryCount = 0;

        establishConnection();
    }

    private void establishConnection() {
        if (currentCourseId == null) return;

        Log.d(TAG, "Establishing WebSocket connection for course: " + currentCourseId);

        // URL WebSocket dari BuildConfig atau environment
        String wsUrl = BuildConfig.REVERB_FORUM_URL; // Sesuaikan dengan config Anda

        Request request = new Request.Builder()
                .url(wsUrl)
                .build();

        webSocket = httpClient.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                Log.d(TAG, "WebSocket connected");
                isConnected = true;
                retryCount = 0;

                if (listener != null) {
                    listener.onConnected();
                }
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                Log.d(TAG, "WebSocket message received: " + text);
                handleWebSocketMessage(text, webSocket);
            }

            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                Log.d(TAG, "WebSocket closed: " + code + " - " + reason);
                isConnected = false;

                if (listener != null) {
                    listener.onDisconnected();
                }

                // Auto reconnect jika tidak normal close
                if (code != 1000 && code != 1001 && retryCount < MAX_RETRIES) {
                    scheduleReconnect();
                }
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
                Log.e(TAG, "WebSocket connection failed", t);
                isConnected = false;

                if (listener != null) {
                    listener.onConnectionError(t.getMessage());
                }

                if (retryCount < MAX_RETRIES) {
                    scheduleReconnect();
                }
            }
        });
    }

    private void handleWebSocketMessage(String message, WebSocket webSocket) {
        try {
            JSONObject json = new JSONObject(message);
            String event = json.optString("event");

            switch (event) {
                case "pusher:connection_established":
                    handleConnectionEstablished(json, webSocket);
                    break;

                case "pusher:subscription_succeeded":
                    Log.d(TAG, "Successfully subscribed to forum channel");
                    break;

                case "forum.sent":
                    handleForumMessage(json);
                    break;

                default:
                    Log.d(TAG, "Unhandled event: " + event);
                    break;
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing WebSocket message", e);
        }
    }

    private void handleConnectionEstablished(JSONObject message, WebSocket webSocket) {
        try {
            JSONObject data = new JSONObject(message.getString("data"));
            socketId = data.optString("socket_id");

            Log.d(TAG, "Socket ID received: " + socketId);

            if (socketId != null && !socketId.isEmpty()) {
                authenticateAndSubscribe(webSocket);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error handling connection established", e);
        }
    }

    private void authenticateAndSubscribe(WebSocket webSocket) {
        String channelName = "presence-forum-" + currentCourseId;
        String authUrl = BuildConfig.REVERB_FORUM_AUTH; // Sesuaikan dengan config

        Log.d(TAG, "Authenticating for channel: " + channelName);

        RequestBody requestBody = new FormBody.Builder()
                .add("socket_id", socketId)
                .add("channel_name", channelName)
                .build();

        Request request = new Request.Builder()
                .url(authUrl)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Auth request failed", e);
                if (listener != null) {
                    listener.onConnectionError("Authentication failed: " + e.getMessage());
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body() != null ? response.body().string() : "";
                Log.d(TAG, "Auth response: " + responseBody);

                if (response.isSuccessful()) {
                    try {
                        JSONObject authData = new JSONObject(responseBody);
                        String auth = authData.optString("auth");
                        String channelData = authData.optString("channel_data");

                        if (!auth.isEmpty()) {
                            subscribeToChannel(webSocket, channelName, auth, channelData);
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing auth response", e);
                    }
                } else {
                    Log.e(TAG, "Auth request failed: " + response.code());
                    if (listener != null) {
                        listener.onConnectionError("Authentication failed: " + response.code());
                    }
                }
            }
        });
    }

    private void subscribeToChannel(WebSocket webSocket, String channelName, String auth, String channelData) {
        try {
            JSONObject subscribeData = new JSONObject();
            subscribeData.put("auth", auth);
            subscribeData.put("channel", channelName);

            if (channelData != null && !channelData.isEmpty()) {
                subscribeData.put("channel_data", channelData);
            }

            JSONObject subscribeMessage = new JSONObject();
            subscribeMessage.put("event", "pusher:subscribe");
            subscribeMessage.put("data", subscribeData);

            Log.d(TAG, "Sending subscribe message: " + subscribeMessage.toString());
            webSocket.send(subscribeMessage.toString());

        } catch (JSONException e) {
            Log.e(TAG, "Error creating subscribe message", e);
        }
    }

    private void handleForumMessage(JSONObject message) {
        try {
            String dataString = message.optString("data");
            if (!dataString.isEmpty()) {
                JSONObject messageData = new JSONObject(dataString);

                String senderId = messageData.optString("username", "");
                String messageText = messageData.optString("message", "");
                String createdAt = messageData.optString("created_at", "");
                String senderName = messageData.optString("name", "");
                String senderRole = messageData.optString("role", "");

                Chat response = new Chat();
                response.setUsername(senderId);
                response.setMessage(messageText);
                response.setCreatedAt(createdAt);
                response.setName(senderName);
                response.setRole(senderRole);

                if (listener != null) {
                    listener.onMessageReceived(response);
                }

            }

        } catch (JSONException e) {
            Log.e(TAG, "Error handling forum message", e);
        }
    }

    private void scheduleReconnect() {
        retryCount++;
        Log.d(TAG, "Scheduling reconnect attempt " + retryCount + "/" + MAX_RETRIES);

        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            if (currentCourseId != null) {
                establishConnection();
            }
        }, RECONNECT_DELAY);
    }

    public void disconnect() {
        if (webSocket != null) {
            Log.d(TAG, "Disconnecting WebSocket");
            webSocket.close(1000, "Normal closure");
            webSocket = null;
        }

        isConnected = false;
        currentCourseId = null;
        socketId = null;
    }

    public boolean isConnected() {
        return isConnected && webSocket != null;
    }

    public String getCurrentCourseId() {
        return currentCourseId;
    }
}