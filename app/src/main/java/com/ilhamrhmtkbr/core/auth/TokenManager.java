package com.ilhamrhmtkbr.core.auth;

import com.ilhamrhmtkbr.core.network.CookieManager;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ✅ SINGLE RESPONSIBILITY: Token operations only
 * - Validate tokens
 * - Get token values
 * - Clear expired tokens
 */
@Singleton
public class TokenManager {
    private final CookieManager cookieManager;

    @Inject
    public TokenManager(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    /**
     * Check apakah ada valid access token
     */
    public boolean hasValidAccessToken() {
        return cookieManager.hasValidAccessToken();
    }

    /**
     * Check apakah ada valid refresh token
     */
    public boolean hasValidRefreshToken() {
        return cookieManager.hasValidRefreshToken();
    }

    /**
     * Check apakah ada session lengkap (access + refresh)
     */
    public boolean hasValidSession() {
        return cookieManager.hasValidSession();
    }

    /**
     * Get access token value (untuk manual header injection)
     */
    public String getAccessToken() {
        return cookieManager.getAccessToken();
    }

    /**
     * Get refresh token value
     */
    public String getRefreshToken() {
        return cookieManager.getRefreshToken();
    }

    /**
     * Clear expired access token
     */
    public void clearExpiredAccessToken() {
        cookieManager.clearExpiredAccessToken();
    }

    /**
     * Clear all tokens (logout)
     */
    public void clearAllTokens() {
        cookieManager.logout();
    }
}