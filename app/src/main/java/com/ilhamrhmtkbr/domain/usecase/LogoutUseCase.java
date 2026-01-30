package com.ilhamrhmtkbr.domain.usecase;

import android.util.Log;
import com.ilhamrhmtkbr.core.auth.TokenManager;
import com.ilhamrhmtkbr.core.auth.SessionRepository;
import com.ilhamrhmtkbr.data.local.database.StudentDatabase;
import com.ilhamrhmtkbr.data.local.database.InstructorDatabase;
import java.util.concurrent.Executors;
import javax.inject.Inject;

/**
 * ✅ SINGLE RESPONSIBILITY: Orchestrate logout flow
 * - Coordinate multiple managers
 * - Clear all user data
 * - Reset app state
 *
 * Ini adalah USE CASE - tempat business logic
 */
public class LogoutUseCase {
    private static final String TAG = "LogoutUseCase";

    private final TokenManager tokenManager;
    private final SessionRepository sessionRepository;
    private final StudentDatabase studentDatabase;
    private final InstructorDatabase instructorDatabase;

    @Inject
    public LogoutUseCase(
            TokenManager tokenManager,
            SessionRepository sessionRepository,
            StudentDatabase studentDatabase,
            InstructorDatabase instructorDatabase
    ) {
        this.tokenManager = tokenManager;
        this.sessionRepository = sessionRepository;
        this.studentDatabase = studentDatabase;
        this.instructorDatabase = instructorDatabase;
    }

    /**
     * Execute logout
     * Coordinate semua cleanup operations
     */
    public void execute(LogoutCallback callback) {
        try {
            // Get role sebelum clear session
            final String role = sessionRepository.getUserRole();

            // 1. Clear tokens (cookies)
            tokenManager.clearAllTokens();
            Log.d(TAG, "✅ Tokens cleared");

            // 2. Clear session data
            sessionRepository.clearSession();
            Log.d(TAG, "✅ Session cleared");

            // 4. Clear database (async)
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    if ("student".equals(role)) {
                        studentDatabase.clearAllTables();
                        Log.d(TAG, "✅ Student database cleared");
                    } else if ("instructor".equals(role)) {
                        instructorDatabase.clearAllTables();
                        Log.d(TAG, "✅ Instructor database cleared");
                    }

                    // Callback on main thread
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error clearing database", e);
                    if (callback != null) {
                        callback.onError(e);
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error during logout", e);
            if (callback != null) {
                callback.onError(e);
            }
        }
    }

    public interface LogoutCallback {
        void onSuccess();
        void onError(Exception e);
    }
}