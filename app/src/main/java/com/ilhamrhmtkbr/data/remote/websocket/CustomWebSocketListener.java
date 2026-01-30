package com.ilhamrhmtkbr.data.remote.websocket;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.core.network.CookieManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class CustomWebSocketListener extends WebSocketListener {

    private static final String TAG = "WebSocket";
    private final WebSocketHandler handler;
    private final String username;
    private final OkHttpClient httpClient;
    private final Context context;
    private String socketId;
    private boolean isSubscribed = false;

    public interface WebSocketHandler {
        void onVerified();
        void onConnectionFailed();
        void onRetryNeeded();
    }

    public CustomWebSocketListener(WebSocketHandler handler, String username, Context context) {
        this.handler = handler;
        this.username = username;
        this.context = context;

        // Use SecureHttpOnlyManager's CookieJar directly
        CookieManager cookieManager = new CookieManager(context);
        this.httpClient = new OkHttpClient.Builder()
                .cookieJar(cookieManager.getCookieJar())
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
        Log.d(TAG, "WebSocket connected successfully");
        isSubscribed = false;
        socketId = null;
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        Log.d(TAG, "Message received: " + text);

        try {
            JSONObject message = new JSONObject(text);
            String event = message.optString("event");

            // Handle connection establishment (similar to pusher:connection_established)
            switch (event) {
                case "pusher:connection_established":
                    handleConnectionEstablished(message, webSocket);
                    break;
                // Handle subscription success
                case "pusher:subscription_succeeded":
                    Log.d(TAG, "Successfully subscribed to channel");
                    isSubscribed = true;
                    break;
                // Handle email verification event
                case "verify.email":
                    handleEmailVerification(message);
                    break;
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing WebSocket message", e);
        }
    }

    private void handleConnectionEstablished(JSONObject message, WebSocket webSocket) {
        try {
            if (message.has("data")) {
                JSONObject data = new JSONObject(message.getString("data"));
                socketId = data.optString("socket_id");
                Log.d(TAG, "Socket ID received: " + socketId);

                if (socketId != null && !socketId.isEmpty()) {
                    authenticateAndSubscribe(webSocket);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error handling connection established", e);
        }
    }

    private void authenticateAndSubscribe(WebSocket webSocket) {
        String channelName = "private-email-verify-" + username;
        String authUrl = BuildConfig.REVERB_USER_AUTH;

        Log.d(TAG, "Authenticating for channel: " + channelName);

        RequestBody requestBody = new FormBody.Builder()
                .add("socket_id", socketId)
                .add("channel_name", channelName)
                .build();

        Request.Builder requestBuilder = new Request.Builder()
                .url(authUrl)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .addHeader("X-Requested-With", "XMLHttpRequest"); // Important for Laravel

        // Add CSRF token if available from SecureHttpOnlyManager
        CookieManager cookieManager = new CookieManager(context);
        String csrfToken = getCSRFTokenFromCookies();
        if (csrfToken != null && !csrfToken.isEmpty()) {
            requestBuilder.addHeader("X-CSRF-TOKEN", csrfToken);
            Log.d(TAG, "Adding CSRF token header");
        }

        // Log cookie status for debugging
        cookieManager.debugStoredCookies();
        Log.d(TAG, "Has valid session: " + cookieManager.hasValidSession());
        Log.d(TAG, "Has valid access token: " + cookieManager.hasValidAccessToken());

        Request request = requestBuilder.build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Auth request failed", e);
                handler.onConnectionFailed();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body() != null ? response.body().string() : "";
                Log.d(TAG, "Auth response code: " + response.code());
                Log.d(TAG, "Auth response body: " + responseBody);

                if (response.isSuccessful()) {
                    try {
                        JSONObject authData = new JSONObject(responseBody);
                        String auth = authData.optString("auth");

                        if (!auth.isEmpty()) {
                            subscribeToChannel(webSocket, channelName, auth);
                        } else {
                            Log.e(TAG, "No auth data received");
                            handler.onConnectionFailed();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parsing auth response", e);
                        handler.onConnectionFailed();
                    }
                } else {
                    Log.e(TAG, "Auth request failed: " + response.code() + " - " + responseBody);
                    handler.onConnectionFailed();
                }
            }
        });
    }

    private String getCSRFTokenFromCookies() {
        // CSRF token biasanya ada di cookie XSRF-TOKEN
        // atau bisa juga dari SharedPreferences jika disimpan terpisah
        SharedPreferences prefs = context.getSharedPreferences("app_session", Context.MODE_PRIVATE);
        return prefs.getString("csrf_token", null);
    }

    private void subscribeToChannel(WebSocket webSocket, String channelName, String auth) {
        try {
            JSONObject subscribeData = new JSONObject();
            subscribeData.put("auth", auth);
            subscribeData.put("channel", channelName);

            JSONObject subscribeMessage = new JSONObject();
            subscribeMessage.put("event", "pusher:subscribe");
            subscribeMessage.put("data", subscribeData);

            Log.d(TAG, "Sending subscribe message: " + subscribeMessage.toString());
            webSocket.send(subscribeMessage.toString());

        } catch (JSONException e) {
            Log.e(TAG, "Error creating subscribe message", e);
            handler.onConnectionFailed();
        }
    }

    private void handleEmailVerification(JSONObject message) {
        try {
            if (message.has("data")) {
                JSONObject data = new JSONObject(message.getString("data"));
                String messageUsername = data.optString("username");

                if (username.equals(messageUsername)) {
                    Log.d(TAG, "Email verified for user: " + username);
                    handler.onVerified();
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error handling email verification", e);
        }
    }

    @Override
    public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        Log.d(TAG, "WebSocket closed: " + code + " - " + reason);
        isSubscribed = false;

        // Don't retry for normal closures
        if (code != 1000 && code != 1001) {
            handler.onRetryNeeded();
        }
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
        Log.e(TAG, "WebSocket error", t);
        isSubscribed = false;
        handler.onRetryNeeded();
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

}