package com.ilhamrhmtkbr.presentation.onboarding;

import android.content.SharedPreferences;

public interface SetupPrefs {
    default boolean isOnboardingCompleted(SharedPreferences sharedPreferences) {
        return sharedPreferences.getBoolean("onboarding_completed", false);
    }

    default void setOnboardingCompletedTrue(SharedPreferences.Editor editor) {
        editor.putBoolean("onboarding_completed", true);
        editor.apply();
    }
}
