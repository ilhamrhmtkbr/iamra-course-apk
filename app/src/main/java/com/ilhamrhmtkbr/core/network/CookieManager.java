package com.ilhamrhmtkbr.core.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.ilhamrhmtkbr.core.network.cookie.SecureCookieJar;

import java.util.List;
import java.util.ArrayList;

import okhttp3.Cookie;
import okhttp3.CookieJar;

public class CookieManager {
    private static final String TAG = "CookieManager";
    private SharedPreferences securePrefs;
    private SecureCookieJar cookieJar;

    public CookieManager(Context context) {
        initializeSecureStorage(context);
        this.cookieJar = new SecureCookieJar(securePrefs);

        Log.d(TAG, "=== COOKIE MANAGER INITIALIZED ===");
    }

    private void initializeSecureStorage(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            securePrefs = EncryptedSharedPreferences.create(
                    "jwt_cookies_secure",
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            e.printStackTrace();
            securePrefs = context.getSharedPreferences("jwt_cookies_fallback", Context.MODE_PRIVATE);
        }
    }

    public void debugStoredCookies() {
        Log.d(TAG, "=== DEBUG STORED COOKIES ===");

        int count = 0;
        for (String key : securePrefs.getAll().keySet()) {
            if (!key.contains("keyset")) {
                if (!key.endsWith("_expiry")) {
                    count++;
                    Log.d(TAG, "Key: " + key);
                    String cookieData = securePrefs.getString(key, null);
                    long expiry = securePrefs.getLong(key + "_expiry", 0);
                    Log.d(TAG, "  Data: " + (cookieData != null ? cookieData.substring(0, Math.min(50, cookieData.length())) + "..." : "null"));
                    Log.d(TAG, "  Expiry: " + new java.util.Date(expiry));
                    Log.d(TAG, "  Expired: " + (System.currentTimeMillis() >= expiry));
                }
            }
        }
        Log.d(TAG, "Total cookies: " + count);
    }

    private Cookie deserializeCookie(String data) {
        try {
            String[] parts = data.split("\\|");
            if (parts.length >= 6) {
                Cookie.Builder builder = new Cookie.Builder()
                        .name(parts[0])
                        .value(parts[1]);

                if (!parts[2].isEmpty()) {
                    builder.domain(parts[2]);
                }
                if (!parts[3].isEmpty()) {
                    builder.path(parts[3]);
                }
                if (Boolean.parseBoolean(parts[4])) {
                    builder.secure();
                }
                if (Boolean.parseBoolean(parts[5])) {
                    builder.httpOnly();
                }

                return builder.build();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error deserializing cookie", e);
        }
        return null;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public boolean hasValidSession() {
        return hasValidAccessToken() && hasValidRefreshToken();
    }

    public boolean hasValidAccessToken() {
        return hasValidToken("access_token");
    }

    public boolean hasValidRefreshToken() {
        return hasValidToken("refresh_token");
    }

    private boolean hasValidToken(String tokenName) {
        for (String key : securePrefs.getAll().keySet()) {
            if (key.contains("_" + tokenName) && !key.endsWith("_expiry") && !key.contains("keyset")) {
                long expiry = securePrefs.getLong(key + "_expiry", 0);
                if (System.currentTimeMillis() < expiry) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getAccessToken() {
        return getTokenValue("access_token");
    }

    public String getRefreshToken() {
        return getTokenValue("refresh_token");
    }

    private String getTokenValue(String tokenName) {
        try {
            for (String key : securePrefs.getAll().keySet()) {
                if (key.contains("_" + tokenName) && !key.endsWith("_expiry") && !key.contains("keyset")) {
                    long expiry = securePrefs.getLong(key + "_expiry", 0);
                    if (System.currentTimeMillis() < expiry) {
                        String cookieData = securePrefs.getString(key, null);
                        if (cookieData != null) {
                            Cookie cookie = deserializeCookie(cookieData);
                            if (cookie != null) {
                                return cookie.value();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting token value", e);
        }
        return null;
    }

    public void logout() {
        cookieJar.clearAllCookies();
    }

    public void clearExpiredAccessToken() {
        try {
            List<String> keysToRemove = new ArrayList<>();

            for (String key : securePrefs.getAll().keySet()) {
                if (key.contains("_access_token") && !key.endsWith("_expiry") && !key.contains("keyset")) {
                    long expiry = securePrefs.getLong(key + "_expiry", 0);
                    if (System.currentTimeMillis() >= expiry) {
                        keysToRemove.add(key);
                        keysToRemove.add(key + "_expiry");
                    }
                }
            }

            if (!keysToRemove.isEmpty()) {
                SharedPreferences.Editor editor = securePrefs.edit();
                for (String key : keysToRemove) {
                    editor.remove(key);
                }
                editor.apply();
                reloadCookiesFromStorage();
                Log.d(TAG, "✅ Cleared " + keysToRemove.size() + " expired access tokens");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error clearing expired tokens", e);
        }
    }

    private void reloadCookiesFromStorage() {
        cookieJar.cookieStore.clear();
    }
}