package com.ilhamrhmtkbr.core.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;

import dagger.hilt.android.qualifiers.ApplicationContext;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SessionRepository {
    private static final String TAG = "SessionRepository";
    private static final String PREFS_NAME = "session_prefs";
    private static final String KEY_ROLE = "user_role";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_MIDDLE_NAME = "middle_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DOB = "dob";
    private static final String KEY_SUMMARY = "summary";
    private static final String KEY_RESUME = "resume";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_EMAIL_VERIFIED_AT = "email_verified_at";

    public static final String INSTRUCTOR = "instructor";
    public static final String STUDENT = "student";
    public static final String USER = "user";
    public static final String GUEST = "guest";
    public static final String MEMBER = "member";
    public static final String FORUM = "forum";

    private final SharedPreferences prefs;

    @Inject
    public SessionRepository(@ApplicationContext Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getUsername() {
        return prefs.getString(KEY_USERNAME, null);
    }

    public String getUserRole() {
        return prefs.getString(KEY_ROLE, null);
    }

    public boolean hasActiveSession() {
        return getUsername() != null && getUserRole() != null;
    }

    public void saveUserSession(UserResponse user) {
        SharedPreferences.Editor editor = prefs.edit();

        if (user.getUsername() != null) {
            editor.putString(KEY_USERNAME, user.getUsername());
        }
        if (user.getRole() != null) {
            editor.putString(KEY_ROLE, user.getRole());
        }
        if (user.getEmail() != null) {
            editor.putString(KEY_EMAIL, user.getEmail());
        }
        if (user.getFull_name() != null) {
            String[] fullName = user.getFull_name().trim().split("\\s+");
            String firstName = "", middleName = "", lastName = "";

            if (fullName.length > 0) {
                firstName = fullName[0];
            }

            if (fullName.length > 2) {
                middleName = fullName[1];
                StringBuilder sb = new StringBuilder();
                for (int i = 2; i < fullName.length; i++) {
                    sb.append(fullName[i]);
                    if (i != fullName.length - 1) sb.append(" ");
                }
                lastName = sb.toString();
            } else if (fullName.length == 2) {
                middleName = fullName[1];
            }

            editor.putString(KEY_FIRST_NAME, firstName);
            editor.putString(KEY_MIDDLE_NAME, middleName);
            editor.putString(KEY_LAST_NAME, lastName);
        }
        if (user.getImage() != null) {
            editor.putString(KEY_IMAGE, user.getImage());
        }
        if (user.getAddress() != null) {
            editor.putString(KEY_ADDRESS, user.getAddress());
        }
        if (user.getDob() != null) {
            editor.putString(KEY_DOB, user.getDob());
        }
        if (user.getSummary() != null) {
            editor.putString(KEY_SUMMARY, user.getSummary());
        }
        if (user.getResume() != null) {
            editor.putString(KEY_RESUME, user.getResume());
        }
        if (user.getCategory() != null) {
            editor.putString(KEY_CATEGORY, user.getCategory());
        }
        if (user.getCreatedAt() != null) {
            editor.putString(KEY_CREATED_AT, user.getCreatedAt());
        }
        if (user.getEmailVerifiedAt() != null) {
            editor.putString(KEY_EMAIL_VERIFIED_AT, user.getEmailVerifiedAt());
        }

        editor.apply();
        Log.d(TAG, "User session saved: " + user.getUsername() + ", role: " + user.getRole());
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    public String getFirstName() {
        return prefs.getString(KEY_FIRST_NAME, null);
    }

    public String getMiddleName() {
        return prefs.getString(KEY_MIDDLE_NAME, null);
    }

    public String getLastName() {
        return prefs.getString(KEY_LAST_NAME, null);
    }

    public String getImage() {
        return prefs.getString(KEY_IMAGE, null);
    }

    public String getAddress() {
        return prefs.getString(KEY_ADDRESS, null);
    }

    public String getDob() {
        return prefs.getString(KEY_DOB, null);
    }

    public String getSummary() {
        return prefs.getString(KEY_SUMMARY, null);
    }

    public String getResume() {
        return prefs.getString(KEY_RESUME, null);
    }

    public String getCategory() {
        return prefs.getString(KEY_CATEGORY, null);
    }

    public String getCreatedAt() {
        return prefs.getString(KEY_CREATED_AT, null);
    }

    public String getEmailVerifiedAt() {
        return prefs.getString(KEY_EMAIL_VERIFIED_AT, null);
    }

    public void clearSession() {
        prefs.edit().clear().apply();
        Log.d(TAG, "Session cleared");
    }
}