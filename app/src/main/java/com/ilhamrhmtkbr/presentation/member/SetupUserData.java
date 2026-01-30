package com.ilhamrhmtkbr.presentation.member;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.auth.SessionRepository;

public interface SetupUserData {
    String TAG = "UserMemberActivity";
    int MAX_RETRY = 2;
    int RETRY_DELAY = 500;

    default void setupUserData(AuthStateManager authManager, Handler handler, NavigationView navigationView) {
        String role = authManager.getCurrentUserRole();

        if (authManager.isLoggedIn() && role != null) {
            setupDynamicMenu(navigationView, authManager);
            setRetryCount(0);
        } else {
            setRetryCount(getRetryCount() + 1);

            if (getRetryCount() <= MAX_RETRY) {
                Log.d(TAG, "User data not ready yet, retry " + getRetryCount() + "/" + MAX_RETRY);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupUserData(authManager, handler, navigationView);
                    }
                }, RETRY_DELAY);
            } else {
                Log.w(TAG, "Max retry reached, setting up menu with default state");
                setupDynamicMenuSafe(navigationView);
                setRetryCount(0);
            }
        }
    }

    default void setupDynamicMenuSafe(NavigationView navigationView) {
        try {
            Menu menu = navigationView.getMenu();
            MenuItem roleMenuItem = menu.findItem(R.id.nav_private);

            roleMenuItem.setVisible(false);
            Log.d(TAG, "Menu set to default (hidden) state");
        } catch (Exception e) {
            Log.e(TAG, "Error in setupDynamicMenuSafe", e);
        }
    }

    default void setupDynamicMenu(NavigationView navigationView, AuthStateManager authManager) {
        try {
            Menu menu = navigationView.getMenu();
            MenuItem roleMenuItem = menu.findItem(R.id.nav_private);

            String currentRole = authManager.getCurrentUserRole();

            if (!authManager.isLoggedIn()) {
                Log.w(TAG, "User data is null or empty in setupDynamicMenu");
                roleMenuItem.setVisible(false);
                return;
            }

            boolean isEmailVerified = !authManager.getCurrentUserEmailVerifiedAt().isEmpty();

            if (currentRole != null && isEmailVerified) {
                if (currentRole.equals(SessionRepository.INSTRUCTOR)) {
                    roleMenuItem.setTitle("Instructor");
                    roleMenuItem.setVisible(true);
                    Log.d(TAG, "Menu configured for Instructor");
                } else if (currentRole.equals(SessionRepository.STUDENT)) {
                    roleMenuItem.setTitle("Student");
                    roleMenuItem.setVisible(true);
                    Log.d(TAG, "Menu configured for Student");
                } else {
                    roleMenuItem.setVisible(false);
                    Log.d(TAG, "Menu hidden - unknown role: " + currentRole);
                }
            } else {
                roleMenuItem.setVisible(false);
                Log.d(TAG, "Menu hidden - role: " + currentRole + ", verified: " + isEmailVerified);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in setupDynamicMenu", e);
        }
    }

    int getRetryCount();
    void setRetryCount(int number);
}
