package com.ilhamrhmtkbr.core.utils.system;

import android.os.Build;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.utils.ui.ThemeUtil;

public class StatusBarUtil {
    public static void setupStatusBarIconColorWithConditionalTheme(AppCompatActivity activity) {
        if (ThemeUtil.isDarkMode(activity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                /* Ambil DecorView dari Window
                 * DecorView = root view paling atas
                 * Semua pengaturan system UI (status bar & navigation bar)
                 * diatur lewat view ini
                 */
                View decor = activity.getWindow().getDecorView();

                /* Reset semua flag System UI ke kondisi default
                 * setSystemUiVisibility(0) artinya:
                 * - Tidak ada flag yang aktif
                 * - LIGHT_STATUS_BAR dihapus
                 * - LIGHT_NAVIGATION_BAR dihapus
                 * - FULLSCREEN dihapus
                 * - IMMERSIVE dihapus
                 *
                 * Hasil:
                 * - Status bar tampil normal
                 * - Navigation bar tampil normal
                 * - Warna icon kembali default (putih)
                 */
                decor.setSystemUiVisibility(0);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = activity.getWindow().getDecorView();
                /*
                 Terang
                 */
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public static void setupStatusBarBackgroundColorTransparent(AppCompatActivity activity) {
        activity.getWindow().setStatusBarColor(
                ContextCompat.getColor(
                        activity,
                        R.color.transparent
                ));
    }

    public static void setupStatusBarBackgroundColor(AppCompatActivity activity, int color) {
        activity.getWindow().setStatusBarColor(
                ContextCompat.getColor(
                        activity,
                        color
                )
        );
    }
}
