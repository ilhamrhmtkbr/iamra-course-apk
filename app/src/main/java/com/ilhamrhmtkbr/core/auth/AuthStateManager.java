package com.ilhamrhmtkbr.core.auth;

import android.util.Log;

import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;
import com.ilhamrhmtkbr.domain.usecase.LogoutUseCase;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ✅ SINGLE RESPONSIBILITY: Hanya manage auth state
 * - Check login status
 * - Get current user role/username
 * - Trigger logout flow (coordinate only, don't do the work)
 */
@Singleton
public class AuthStateManager {
    private final TokenManager tokenManager;
    private final SessionRepository sessionRepository;
    private final LogoutUseCase logoutUseCase;

    @Inject
    public AuthStateManager(
            TokenManager tokenManager,
            SessionRepository sessionRepository,
            LogoutUseCase logoutUseCase
    ) {
        this.tokenManager = tokenManager;
        this.sessionRepository = sessionRepository;
        this.logoutUseCase = logoutUseCase;
    }

    /**
     * Check apakah user logged in
     * Logic: ada valid access token + ada session data
     */
    public boolean isLoggedIn() {
        return tokenManager.hasValidAccessToken()
                && sessionRepository.hasActiveSession();
    }

    /**
     * Get username dari session
     */
    public String getCurrentUsername() {
        return sessionRepository.getUsername();
    }

    /**
     * Get role dari session yang tersimpan
     */
    public String getCurrentUserRole() {
        return sessionRepository.getUserRole();
    }

    /**
     * Get email dari session yang tersimpan
     */
    public String getCurrentUserEmail() {
        return sessionRepository.getEmail();
    }

    /**
     * Get fullName dari session yang tersimpan
     */
    public String getCurrentUserFullName() {
        return sessionRepository.getFirstName() + " " + sessionRepository.getMiddleName() + " " + sessionRepository.getLastName();
    }

    public String getCurrentUserFirstName() {
        return sessionRepository.getFirstName();
    }

    public String getCurrentUserMiddleName() {
        return sessionRepository.getMiddleName();
    }

    public String getCurrentUserLastName() {
        return sessionRepository.getLastName();
    }

    /**
     * Get image dari session yang tersimpan
     */
    public String getCurrentUserImage() {
        return sessionRepository.getImage();
    }

    /**
     * Get address dari session yang tersimpan
     */
    public String getCurrentUserAddress() {
        return sessionRepository.getAddress();
    }

    /**
     * Get dob dari session yang tersimpan
     */
    public String getCurrentUserDob() {
        return sessionRepository.getDob();
    }

    /**
     * Get summary dari session yang tersimpan
     */
    public String getCurrentUserSummary() {
        return sessionRepository.getSummary();
    }

    /**
     * Get resume dari session yang tersimpan
     */
    public String getCurrentUserResume() {
        return sessionRepository.getResume();
    }

    /**
     * Get category dari session yang tersimpan
     */
    public String getCurrentUserCategory() {
        return sessionRepository.getCategory();
    }

    /**
     * Get createdAt dari session yang tersimpan
     */
    public String getCurrentUserCreatedAt() {
        return sessionRepository.getCreatedAt();
    }

    /**
     * Get emailVerifiedAt dari session yang tersimpan
     */
    public String getCurrentUserEmailVerifiedAt() {
        return sessionRepository.getEmailVerifiedAt();
    }

    /**
     * Check apakah user adalah instructor
     */
    public boolean isInstructor() {
        return SessionRepository.INSTRUCTOR.equals(getCurrentUserRole());
    }

    /**
     * Check apakah user adalah student
     */
    public boolean isStudent() {
        return SessionRepository.STUDENT.equals(getCurrentUserRole());
    }

    public void saveUserSession(UserResponse userResponse) {
        sessionRepository.saveUserSession(userResponse);
    }

    public void logout() {
        logoutUseCase.execute(new LogoutUseCase.LogoutCallback() {
            @Override
            public void onSuccess() {
                Log.d("AuthStateManager", "Logout Success");
            }

            @Override
            public void onError(Exception e) {
                Log.d("AuthStateManager", e.getMessage());
            }
        });
    }
}