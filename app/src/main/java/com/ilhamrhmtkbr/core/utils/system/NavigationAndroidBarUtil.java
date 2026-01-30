package com.ilhamrhmtkbr.core.utils.system;

import android.os.Build;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class NavigationAndroidBarUtil {

    /**
     * Di pake buat mengatur warna background navigation bar
     * bawaan android (Back, Home, Recent),
     * BUKAN BottomNavigationView !
     */
    public static void setupNavigationAndroidBarBackgroundColor(
            AppCompatActivity activity, int color) {
        /*
         * Ambil Window dari Activity
         * Window = wadah utama tampilan Activity
         * Semua komponen system UI (status bar & navigation bar)
         * diatur lewat object Window ini
         */
        activity.getWindow()
                /*
                 * Set warna background navigation bar (bagian bawah layar)
                 * Yang berubah hanya BACKGROUND-nya, bukan icon/tombolnya
                 */
                .setNavigationBarColor(
                        ContextCompat.getColor(
                                activity,
                                color
                        )
                );
    }

    /**
     * Di pake buat mengatur warna icon navigation bar
     * bawaan android (Back, Home, Recent),
     * BUKAN BottomNavigationView !
     */
    public static void setupNavigationAndroidBarIconColor(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Ambil root view window
             * DecorView = view paling atas dari seluruh tampilan AppCompatActivity
             * Semua pengaturan system UI (status bar, nav bar) diatur dari sini
             */
            View decor = activity.getWindow().getDecorView();
            /* Ambil flag system UI yang aktif
             * flags berisi konfigurasi UI saat ini
             * Contoh flag:
             * LIGHT_STATUS_BAR
             * LIGHT_NAVIGATION_BAR
             * FULLSCREEN
             * dll
             */
            int flags = decor.getSystemUiVisibility();
            /* Menghapus LIGHT_NAVIGATION_BAR
             * Apa itu SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR?
             * Artinya: ikon navigation bar berwarna gelap
             * Biasanya dipakai kalau background navigation bar terang (putih)
             * Kenapa dihapus?
             * ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
             * ➡ Menghapus flag tersebut berarti:
             * Ikon navigation bar jadi PUTIH
             * Cocok untuk navigation bar berwarna gelap
             */
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            /* Terapkan ulang flag ke UI
             * Update UI dengan konfigurasi baru
             * Perubahan langsung terlihat
             */
            decor.setSystemUiVisibility(flags);
        }
    }
}
