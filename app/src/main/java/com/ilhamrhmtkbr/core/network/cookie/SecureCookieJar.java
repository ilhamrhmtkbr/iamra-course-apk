package com.ilhamrhmtkbr.core.network.cookie;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class SecureCookieJar implements CookieJar {
    private final String TAG = "SecureCookieJar";
    public final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    private final SharedPreferences securePrefs;

    public SecureCookieJar(SharedPreferences securePrefs) {
        this.securePrefs = securePrefs;
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        List<Cookie> jwtCookies = new ArrayList<>();

        Log.d(TAG, "=== SAVING COOKIES FOR: " + url.toString() + " ===");
        Log.d(TAG, "Host: " + url.host());
        Log.d(TAG, "Total cookies received: " + cookies.size());

        for (Cookie cookie : cookies) {
            Log.d(TAG, "Cookie: " + cookie.name() + " = " + cookie.value());
            Log.d(TAG, "Domain: " + cookie.domain() + ", Path: " + cookie.path());
            Log.d(TAG, "Expires: " + new java.util.Date(cookie.expiresAt()));

            if (isJwtCookie(cookie)) {
                jwtCookies.add(cookie);

                String storageKey = getStorageKey(url.host(), cookie);
                saveJwtCookie(storageKey, cookie);
                Log.d(TAG, "✅ JWT Cookie saved: " + storageKey);
            }
        }

        if (!jwtCookies.isEmpty()) {
            String cacheKey = getCacheKey(url.host());
            cookieStore.put(cacheKey, jwtCookies);
            Log.d(TAG, "Cached " + jwtCookies.size() + " cookies for: " + cacheKey);
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        Log.d(TAG, "=== LOADING COOKIES FOR: " + url.toString() + " ===");
        Log.d(TAG, "Host: " + url.host());

        String cacheKey = getCacheKey(url.host());
        List<Cookie> cookies = cookieStore.get(cacheKey);

        if (cookies == null) {
            Log.d(TAG, "No cookies in memory, loading from storage...");
            cookies = loadJwtCookies(url.host());

            if (cookies != null && !cookies.isEmpty()) {
                cookieStore.put(cacheKey, cookies);
                Log.d(TAG, "✅ Loaded " + cookies.size() + " cookies from storage");
            } else {
                Log.w(TAG, "⚠️ No cookies found for: " + cacheKey);
            }
        } else {
            Log.d(TAG, "Found " + cookies.size() + " cookies in memory");
        }

        List<Cookie> validCookies = new ArrayList<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieMatchesUrl(cookie, url) && !isExpired(cookie)) {
                    validCookies.add(cookie);
                    Log.d(TAG, "✅ Sending: " + cookie.name());
                } else {
                    Log.d(TAG, "❌ Skipping: " + cookie.name() + " (expired or not matching)");
                }
            }
        }

        return validCookies;
    }

    private String getStorageKey(String host, Cookie cookie) {
        String domain = getCookieDomain(host, cookie);
        return domain + "_" + cookie.name();
    }

    private String getCacheKey(String host) {
        if (isLocalhost(host)) {
            return host;
        }
        return extractBaseDomain(host);
    }

    private String getCookieDomain(String host, Cookie cookie) {
        String cookieDomain = cookie.domain();
        if (cookieDomain != null && !cookieDomain.isEmpty()) {
            String cleaned = cookieDomain.startsWith(".") ?
                    cookieDomain.substring(1) : cookieDomain;

            if (isLocalhost(cleaned)) {
                return host;
            }
            return cleaned;
        }

        return getCacheKey(host);
    }

    private boolean cookieMatchesUrl(Cookie cookie, HttpUrl url) {
        String cookieDomain = cookie.domain();
        String urlHost = url.host();

        if (isLocalhost(urlHost)) {
            Log.d(TAG, "Localhost detected, allowing cookie: " + cookie.name());
            return true;
        }

        if (cookieDomain != null && !cookieDomain.isEmpty()) {
            if (cookieDomain.startsWith(".")) {
                String baseDomain = cookieDomain.substring(1);
                boolean matches = urlHost.endsWith(baseDomain) || urlHost.equals(baseDomain);
                Log.d(TAG, "Domain cookie check: " + cookieDomain + " vs " + urlHost + " = " + matches);
                return matches;
            } else {
                String urlBaseDomain = extractBaseDomain(urlHost);
                boolean matches = urlBaseDomain.equals(cookieDomain);
                Log.d(TAG, "Host cookie check: " + cookieDomain + " vs base " + urlBaseDomain + " = " + matches);
                return matches;
            }
        }

        String urlBaseDomain = extractBaseDomain(urlHost);
        String cookieBaseDomain = extractBaseDomain(cookieDomain != null ? cookieDomain : "");
        boolean matches = urlBaseDomain.equals(cookieBaseDomain);
        Log.d(TAG, "Fallback domain check: " + urlBaseDomain + " vs " + cookieBaseDomain + " = " + matches);
        return matches;
    }

    private boolean isLocalhost(String host) {
        return host.equals("localhost") ||
                host.equals("127.0.0.1") ||
                host.equals("10.0.2.2") ||
                host.startsWith("192.168.") ||
                host.startsWith("10.");
    }


    private String extractBaseDomain(String host) {
        if (isLocalhost(host)) {
            return host;
        }

        String[] parts = host.split("\\.");
        if (parts.length >= 2) {
            return parts[parts.length - 2] + "." + parts[parts.length - 1];
        }
        return host;
    }

    private boolean isJwtCookie(Cookie cookie) {
        String name = cookie.name().toLowerCase();
        return name.equals("access_token") || name.equals("refresh_token");
    }

    private boolean isExpired(Cookie cookie) {
        return System.currentTimeMillis() >= cookie.expiresAt();
    }

    public void clearAllCookies() {
        cookieStore.clear();
        clearStoredCookies();
        Log.d(TAG, "✅ All cookies cleared");
    }

    private void clearStoredCookies() {
        SharedPreferences.Editor editor = securePrefs.edit();
        editor.clear();
        editor.apply();
    }

    private void saveJwtCookie(String storageKey, Cookie cookie) {
        try {
            String value = serializeCookie(cookie);
            SharedPreferences.Editor editor = securePrefs.edit();
            editor.putString(storageKey, value);
            editor.putLong(storageKey + "_expiry", cookie.expiresAt());
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, "Error saving cookie", e);
        }
    }

    private List<Cookie> loadJwtCookies(String host) {
        List<Cookie> cookies = new ArrayList<>();

        try {
            String searchPattern = getCacheKey(host);
            Log.d(TAG, "Loading cookies matching: " + searchPattern);

            for (String key : securePrefs.getAll().keySet()) {
                if (key.startsWith(searchPattern + "_") && !key.endsWith("_expiry") && !key.contains("keyset")) {
                    String cookieData = securePrefs.getString(key, null);
                    long expiry = securePrefs.getLong(key + "_expiry", 0);

                    if (cookieData != null && System.currentTimeMillis() < expiry) {
                        Cookie cookie = deserializeCookie(cookieData);
                        if (cookie != null) {
                            cookies.add(cookie);
                            Log.d(TAG, "✅ Loaded: " + cookie.name() + " from key: " + key);
                        }
                    } else if (cookieData != null && System.currentTimeMillis() >= expiry) {
                        SharedPreferences.Editor editor = securePrefs.edit();
                        editor.remove(key);
                        editor.remove(key + "_expiry");
                        editor.apply();
                        Log.d(TAG, "🗑️ Removed expired: " + key);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading cookies", e);
        }

        return cookies;
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

    private String serializeCookie(Cookie cookie) {
        StringBuilder builder = new StringBuilder();
        builder.append(cookie.name()).append("|");
        builder.append(cookie.value()).append("|");
        builder.append(cookie.domain() != null ? cookie.domain() : "").append("|");
        builder.append(cookie.path() != null ? cookie.path() : "").append("|");
        builder.append(cookie.secure()).append("|");
        builder.append(cookie.httpOnly());
        return builder.toString();
    }

    public void clearExpiredTokens() {
        List<String> expiredKeys = new ArrayList<>();

        for (String key : securePrefs.getAll().keySet()) {
            if (!key.endsWith("_expiry") && !key.contains("keyset")) {
                long expiry = securePrefs.getLong(key + "_expiry", 0);
                if (System.currentTimeMillis() >= expiry) {
                    expiredKeys.add(key);
                }
            }
        }

        if (!expiredKeys.isEmpty()) {
            SharedPreferences.Editor editor = securePrefs.edit();
            for (String key : expiredKeys) {
                editor.remove(key);
                editor.remove(key + "_expiry");
            }
            editor.apply();
            cookieStore.clear();
            Log.d(TAG, "✅ Cleared " + expiredKeys.size() + " expired cookies");
        }
    }
}