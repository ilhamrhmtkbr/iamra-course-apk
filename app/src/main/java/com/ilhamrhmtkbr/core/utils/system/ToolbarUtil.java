package com.ilhamrhmtkbr.core.utils.system;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

public class ToolbarUtil {
    /**
     * Di pake buat mengatur warna icon overflow (titik 3) di Toolbar
     */
    public static void setupToolbarOverflowIconColor(
            AppCompatActivity activity,
            Toolbar toolbar,
            int color
    ) {
        /* Ambil icon overflow (ikon titik tiga / more)
         * Icon ini biasanya muncul di pojok kanan Toolbar
         * dan menandakan menu tambahan
         */
        Drawable overflow = toolbar.getOverflowIcon();
        /* Cek null untuk mencegah crash
         * Overflow icon bisa null kalau:
         * - menu belum di-set
         * - toolbar belum inflate menu
         */
        if (overflow != null) {
            /* Set warna (tint) icon overflow
             * Yang diubah hanya WARNA icon-nya,
             * bukan background Toolbar
             */
            overflow.setTint(
                    /* Ambil warna dari resource color
                     * ContextCompat dipakai agar aman
                     * di semua versi Android
                     */
                    ContextCompat.getColor(
                            activity, /* context dari activity */
                            color     /* warna icon overflow */
                    )
            );
        }
    }

    /**
     * Di pake buat mengatur warna icon Navigation (garis 3) di Toolbar
     */
    public static void setupToolbarNavigationIconColor(AppCompatActivity activity, Toolbar toolbar, int color) {
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setColorFilter(
                    ContextCompat.getColor(activity, color),
                    PorterDuff.Mode.SRC_ATOP
            );
        }
    }
}
