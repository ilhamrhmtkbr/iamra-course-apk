package com.ilhamrhmtkbr.core.utils.notif;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.ilhamrhmtkbr.R;

public class DialogUtil {
    public interface DialogCallback {
        void onPositive();
        default void onNegative() {}
        default void onNeutral() {}
    }

    // Simple Alert Dialog
    public static void showAlert(Context context, String title, String message, DialogCallback callback) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (callback != null) callback.onPositive();
                })
                .setCancelable(false)
                .show();
    }

    // Confirmation Dialog
    public static void showConfirmation(Context context, String title, String message,
                                        String positiveText, String negativeText, DialogCallback callback) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, (dialog, which) -> {
                    if (callback != null) callback.onPositive();
                })
                .setNegativeButton(negativeText, (dialog, which) -> {
                    if (callback != null) callback.onNegative();
                })
                .setCancelable(false)
                .show();
    }

    // Loading Dialog
    public static androidx.appcompat.app.AlertDialog showLoadingDialog(Context context, String message) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        TextView messageText = view.findViewById(R.id.tv_loading_message);
        messageText.setText(message);

        return new MaterialAlertDialogBuilder(context)
                .setView(view)
                .setCancelable(false)
                .create();
    }

    // Success Dialog with Icon
    public static void showSuccess(Context context, String title, String message, DialogCallback callback) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_custom_check_circle)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (callback != null) callback.onPositive();
                })
                .show();
    }

    // Error Dialog with Icon
    public static void showError(Context context, String title, String message, DialogCallback callback) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_custom_error)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (callback != null) callback.onPositive();
                })
                .show();
    }

    // Warning Dialog
    public static void showWarning(Context context, String title,
                                   String message, DialogCallback callback) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(R.drawable.ic_custom_warning)
                .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {
                    if (callback != null) callback.onPositive();
                })
                .setNegativeButton(context.getString(R.string.cancel), (dialog, which) -> {
                    if (callback != null) callback.onNegative();
                })
                .show();
    }

    // Course Download Dialog
    public static androidx.appcompat.app.AlertDialog showDownloadDialog(Context context, String courseName, String size,
                                                                        DialogCallback callback) {
        String message = "Download materi kursus \"" + courseName + "\"?\n\nUkuran: " + size;

        return new MaterialAlertDialogBuilder(context)
                .setTitle("Download Materi")
                .setMessage(message)
                .setIcon(R.drawable.ic_custom_download)
                .setPositiveButton("Download", (dialog, which) -> {
                    if (callback != null) callback.onPositive();
                })
                .setNegativeButton("Batal", (dialog, which) -> {
                    if (callback != null) callback.onNegative();
                })
                .show();
    }

    // Logout Confirmation Dialog
    public static androidx.appcompat.app.AlertDialog showLogoutDialog(Context context, DialogCallback callback) {
        return new MaterialAlertDialogBuilder(context)
                .setTitle("Keluar Akun")
                .setMessage("Apakah Anda yakin ingin keluar dari akun?")
                .setIcon(R.drawable.ic_drawer_logout)
                .setPositiveButton("Keluar", (dialog, which) -> {
                    if (callback != null) callback.onPositive();
                })
                .setNegativeButton("Batal", (dialog, which) -> {
                    if (callback != null) callback.onNegative();
                })
                .show();
    }

    // Snackbar Utilities
    public static void showSuccessSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();

        snackbarView.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.bg_blue_radiuss));

        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(view.getContext().getResources().getColor(R.color.white));

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbarView.getLayoutParams();
        params.setMargins(32, 175, 32, 0);

        // Set gravity hanya jika params adalah FrameLayout.LayoutParams atau CoordinatorLayout.LayoutParams
        if (params instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) params).gravity = Gravity.TOP;
        } else if (params instanceof CoordinatorLayout.LayoutParams) {
            ((CoordinatorLayout.LayoutParams) params).gravity = Gravity.TOP;
        }

        snackbarView.setLayoutParams(params);
        snackbar.show();
    }


    public static void showErrorSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();

        snackbarView.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.bg_red_radiuss));

        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(view.getContext().getResources().getColor(R.color.white));

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbarView.getLayoutParams();
        params.setMargins(32, 175, 32, 0);

        // Set gravity hanya jika params adalah FrameLayout.LayoutParams atau CoordinatorLayout.LayoutParams
        if (params instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) params).gravity = Gravity.TOP;
        } else if (params instanceof CoordinatorLayout.LayoutParams) {
            ((CoordinatorLayout.LayoutParams) params).gravity = Gravity.TOP;
        }

        snackbarView.setLayoutParams(params);
        snackbar.show();
    }
}