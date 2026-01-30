package com.ilhamrhmtkbr.core.utils.ui;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class ThemeUtil {
    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_THEME = "theme_mode";

    // Theme modes
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final int THEME_SYSTEM = 2;

    private final SharedPreferences prefs;

    @Inject
    public ThemeUtil(@ApplicationContext Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Simpan tema yang dipilih
     */
    public void setTheme(int themeMode) {
        prefs.edit().putInt(KEY_THEME, themeMode).apply();
        applyTheme(themeMode);
    }

    /**
     * Ambil tema yang tersimpan
     */
    public int getTheme() {
        return prefs.getInt(KEY_THEME, THEME_SYSTEM); // Default: System
    }

    /**
     * Terapkan tema ke aplikasi
     */
    public void applyTheme(int themeMode) {
        switch (themeMode) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    /**
     * Apply saved theme (panggil di onCreate Application atau MainActivity)
     */
    public void applySavedTheme() {
        applyTheme(getTheme());
    }

    /**
     * Check apakah sedang dark mode
     */
    public static boolean isDarkMode(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode
                & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES;
    }
}