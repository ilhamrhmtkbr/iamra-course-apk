package com.ilhamrhmtkbr.core.utils.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

public class LayoutUtil {
    public static void setupLayoutFullscreen(AppCompatActivity activity) {
        WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);
    }
}
