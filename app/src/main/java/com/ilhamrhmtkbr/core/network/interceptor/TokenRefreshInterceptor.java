package com.ilhamrhmtkbr.core.network.interceptor;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.network.CookieManager;
import com.ilhamrhmtkbr.data.remote.api.UserApi;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TokenRefreshInterceptor implements Interceptor {
    private static final String TAG = "TokenRefreshInterceptor";
    private AuthStateManager authStateManager;
    private UserApi userApi;
    private CookieManager cookieManager;

    @Inject
    public TokenRefreshInterceptor(AuthStateManager authStateManager, UserApi userApi, CookieManager cookieManager) {
        this.authStateManager = authStateManager;
        this.userApi = userApi;
        this.cookieManager = cookieManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        String path = request.url().encodedPath();
        int code = response.code();
        Log.d(TAG, "Intercepted request: " + path + " | Response code: " + code);

        if (path.contains("/auth/me")) {
            Log.d(TAG, "Detected /auth/me endpoint");

            if (response.isSuccessful()) {
                Log.d(TAG, "Response is successful, calling handleAuthMeResponse");
                handleAuthMeResponse(response);
            } else {
                Log.e(TAG, "Response NOT successful for /auth/me! Code: " + code);
            }
        }

        if (response.code() == 401) {
            return handleUnauthorizedResponse(chain, request, response);
        }

        return response;
    }

    private void handleAuthMeResponse(Response response) {
        try {
            Log.d(TAG, "=== HANDLING AUTH/ME RESPONSE ===");

            ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
            String responseString = responseBody.string();

            Log.d(TAG, "Response body: " + responseString);

            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(responseString).getAsJsonObject();

            if (!jsonObject.has("data")) {
                Log.e(TAG, "Response does not have 'data' field!");
                Log.e(TAG, "Full response: " + jsonObject.toString());
                return;
            }

            JsonObject userData = jsonObject.getAsJsonObject("data");
            Log.d(TAG, "User data object: " + userData.toString());

            UserResponse userResponse = new UserResponse();

            if (userData.has("username") && !userData.get("username").isJsonNull()) {
                String username = userData.get("username").getAsString();
                userResponse.setUsername(username);
                Log.d(TAG, "Set username: " + username);
            } else {
                Log.w(TAG, "username is null or missing");
            }

            if (userData.has("full_name") && !userData.get("full_name").isJsonNull()) {
                String fullName = userData.get("full_name").getAsString();
                userResponse.setFull_name(fullName);
                Log.d(TAG, "Set full_name: " + fullName);
            } else {
                Log.w(TAG, "full_name is null or missing");
            }

            if (userData.has("email") && !userData.get("email").isJsonNull()) {
                String email = userData.get("email").getAsString();
                userResponse.setEmail(email);
                Log.d(TAG, "Set email: " + email);
            } else {
                Log.w(TAG, "email is null or missing");
            }

            if (userData.has("email_verified_at") && !userData.get("email_verified_at").isJsonNull()) {
                String emailVerified = userData.get("email_verified_at").getAsString();
                userResponse.setEmailVerifiedAt(emailVerified);
                Log.d(TAG, "Set email_verified_at: " + emailVerified);
            } else {
                Log.w(TAG, "email_verified_at is null or missing");
            }

            if (userData.has("image") && !userData.get("image").isJsonNull()) {
                String image = userData.get("image").getAsString();
                userResponse.setImage(image);
                Log.d(TAG, "Set image: " + image);
            } else {
                Log.w(TAG, "image is null or missing");
            }

            if (userData.has("address") && !userData.get("address").isJsonNull()) {
                String address = userData.get("address").getAsString();
                userResponse.setAddress(address);
                Log.d(TAG, "Set address: " + address);
            } else {
                Log.w(TAG, "address is null or missing");
            }

            if (userData.has("created_at") && !userData.get("created_at").isJsonNull()) {
                String createdAt = userData.get("created_at").getAsString();
                userResponse.setCreatedAt(createdAt);
                Log.d(TAG, "Set created_at: " + createdAt);
            } else {
                Log.w(TAG, "created_at is null or missing");
            }

            if (userData.has("role") && !userData.get("role").isJsonNull()) {
                String role = userData.get("role").getAsString();
                userResponse.setRole(role);
                Log.d(TAG, "✅ Set role: " + role);
            } else {
                Log.e(TAG, "⚠️ CRITICAL: role is null or missing!");
                Log.e(TAG, "userData keys: " + userData.keySet().toString());
            }

            if (userData.has("dob") && !userData.get("dob").isJsonNull()) {
                String dob = userData.get("dob").getAsString();
                userResponse.setDob(dob);
                Log.d(TAG, "Set dob: " + dob);
            } else {
                Log.w(TAG, "dob is null or missing");
            }

            if (userData.has("summary") && !userData.get("summary").isJsonNull()) {
                String summary = userData.get("summary").getAsString();
                userResponse.setSummary(summary);
                Log.d(TAG, "Set summary: " + summary);
            } else {
                Log.w(TAG, "summary is null or missing");
            }

            if (userData.has("resume") && !userData.get("resume").isJsonNull()) {
                String resume = userData.get("resume").getAsString();
                userResponse.setResume(resume);
                Log.d(TAG, "Set resume: " + resume);
            } else {
                Log.w(TAG, "resume is null or missing");
            }

            if (userData.has("category") && !userData.get("category").isJsonNull()) {
                String category = userData.get("category").getAsString();
                userResponse.setCategory(category);
                Log.d(TAG, "Set category: " + category);
            } else {
                Log.w(TAG, "category is null or missing");
            }

            Log.d(TAG, "=== USER MODEL BEFORE SAVE ===");
            Log.d(TAG, "Username: " + userResponse.getUsername());
            Log.d(TAG, "Email: " + userResponse.getEmail());
            Log.d(TAG, "Role: " + userResponse.getRole());
            Log.d(TAG, "Full Name: " + userResponse.getFull_name());

            if (userResponse.getRole() == null || userResponse.getRole().isEmpty()) {
                Log.e(TAG, "⚠️ WARNING: Attempting to save UserModel with NULL role!");
                Log.e(TAG, "This will cause issues in UserMemberActivity!");
            }

            authStateManager.saveUserSession(userResponse);
            Log.d(TAG, "✅ User data saved successfully");

        } catch (Exception e) {
            Log.e(TAG, "❌ Error handling auth/me response", e);
            Log.e(TAG, "Exception type: " + e.getClass().getName());
            Log.e(TAG, "Exception message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Response handleUnauthorizedResponse(Chain chain, Request request, Response response) throws IOException {
        String requestPath = request.url().encodedPath();

        if (requestPath.contains("/auth/refresh")) {
            Log.w(TAG, "Refresh token failed, redirecting to login");
            redirectToLogin();
            return response;
        }

        try {
            Log.d(TAG, "Attempting token refresh due to 401");
            boolean refreshSuccess = attemptTokenRefresh();

            if (refreshSuccess) {
                Log.d(TAG, "Token refreshed successfully, retrying original request");
                response.close();
                return chain.proceed(request);
            } else {
                Log.w(TAG, "Token refresh failed, redirecting to login");
                redirectToLogin();
                return response;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error during token refresh", e);
            redirectToLogin();
            return response;
        }
    }

    private boolean attemptTokenRefresh() {
        try {
            retrofit2.Response<BaseResponseApi<String>> response = userApi.refreshToken().execute();

            if (response.isSuccessful() && response.body() != null) {
                Log.d(TAG, "Token refresh successful");
                return true;
            } else {
                Log.w(TAG, "Token refresh failed with code: " + response.code());
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error refreshing token", e);
            return false;
        }
    }

    private void redirectToLogin() {
        try {
            cookieManager.clearExpiredAccessToken();
        } catch (Exception e) {
            Log.e(TAG, "Error redirecting to login", e);
        }
    }
}