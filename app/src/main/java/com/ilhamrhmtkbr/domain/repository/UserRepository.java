package com.ilhamrhmtkbr.domain.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;
import com.ilhamrhmtkbr.domain.model.User;
import org.json.JSONObject;
import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ✅ SINGLE RESPONSIBILITY: User data operations
 * - Save/load user profile data
 * - No auth logic, no token management
 */
@Singleton
public class UserRepository {
    private static final String TAG = "UserRepository";
    private static final String PREFS_NAME = "user_data_prefs";
    private static final String KEY_USER_DATA = "user_data";

    private final SharedPreferences prefs;

    @Inject
    public UserRepository(@ApplicationContext Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Save user profile data
     */
    public void saveUserData(User user) {
        try {
            prefs.edit()
                    .putString(KEY_USER_DATA, user.toString())
                    .apply();
            Log.d(TAG, "User data saved: " + user.getEmail());
        } catch (Exception e) {
            Log.e(TAG, "Error saving user data", e);
        }
    }

    /**
     * Get user profile data
     */
    public JSONObject getUserData() {
        try {
            String userData = prefs.getString(KEY_USER_DATA, null);
            if (userData != null && !userData.isEmpty()) {
                JSONObject userJson = UserResponse.fromString(userData);
                if (userJson != null && userJson.length() > 0) {
                    return userJson;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving user data", e);
        }
        return null;
    }

    /**
     * Clear user data
     */
    public void clearUserData() {
        prefs.edit().clear().apply();
        Log.d(TAG, "User data cleared");
    }

    /**
     * Check apakah ada user data tersimpan
     */
    public boolean hasUserData() {
        return getUserData() != null;
    }
}