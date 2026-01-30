package com.ilhamrhmtkbr.presentation.member;

import android.view.View;

import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public interface SetupExtraListener {
    default void setupScrollListener(NestedScrollView nestedScrollView, BottomNavigationView bottomNavView) {
        nestedScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            // Scroll down (scrolling up content) - hide bottom nav
            if (scrollY > oldScrollY && scrollY > 10) {
                if (getConditionIsBottomNavVisible()) {
                    hideBottomNav(bottomNavView);
                }
            }
            // Scroll up (scrolling down content) - show bottom nav
            else if (scrollY < oldScrollY) {
                if (!getConditionIsBottomNavVisible()) {
                    showBottomNav(bottomNavView);
                }
            }
        });
    }

    default void hideBottomNav(BottomNavigationView bottomNavView) {
        setIsBottomNavVisible(false);
        bottomNavView.animate()
                .translationY(bottomNavView.getHeight())
                .setDuration(300)
                .start();
    }

    default void showBottomNav(BottomNavigationView bottomNavView) {
        setIsBottomNavVisible(true);
        bottomNavView.animate()
                .translationY(0)
                .setDuration(300)
                .start();
    }

    boolean getConditionIsBottomNavVisible();
    void setIsBottomNavVisible(boolean condition);
}
