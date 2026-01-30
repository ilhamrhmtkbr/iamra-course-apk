package com.ilhamrhmtkbr.core.utils.tools;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Just-In-Time Permission Handler
 * Simple utility untuk handle permission saat dibutuhkan
 */
public class PermissionUtil {

    // Request codes untuk berbagai permission
    public static final int REQUEST_CAMERA = 100;
    public static final int REQUEST_STORAGE = 101;
    public static final int REQUEST_MEDIA_IMAGES = 102;
    public static final int REQUEST_AUDIO = 103;
    public static final int REQUEST_NOTIFICATION = 104;
    public static final int REQUEST_LOCATION = 105;

    // ============================================
    // CHECK PERMISSION
    // ============================================

    /**
     * Cek apakah permission sudah granted
     */
    public static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Cek apakah permission permanently denied (user pilih "Don't ask again")
     */
    public static boolean isPermanentlyDenied(Activity activity, String permission) {
        return !hasPermission(activity, permission)
                && !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    // ============================================
    // REQUEST PERMISSION - JUST IN TIME
    // ============================================

    /**
     * Request Camera Permission
     * Panggil saat user mau ambil foto
     */
    public static void requestCamera(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CAMERA);
    }

    /**
     * Request Storage Permission (Android 12 ke bawah)
     * Panggil saat user mau pilih file/gambar
     */
    public static void requestStorage(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ pakai Media permissions
            requestMediaImages(activity);
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE);
        }
    }

    /**
     * Request Media Images Permission (Android 13+)
     * Panggil saat user mau pilih gambar dari galeri
     */
    public static void requestMediaImages(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    REQUEST_MEDIA_IMAGES);
        } else {
            requestStorage(activity);
        }
    }

    /**
     * Request Audio Permission
     * Panggil saat user mau record audio/voice note
     */
    public static void requestAudio(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_AUDIO);
    }

    /**
     * Request Notification Permission (Android 13+)
     * Panggil saat pertama kali user login atau di onboarding
     */
    public static void requestNotification(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_NOTIFICATION);
        }
    }

    /**
     * Request Location Permission
     * Panggil saat user mau scan QR code atau fitur berbasis lokasi
     */
    public static void requestLocation(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION);
    }

    // ============================================
    // HELPER - CHECK & REQUEST
    // ============================================

    /**
     * Check Camera, jika belum granted langsung request
     * @return true jika sudah granted, false jika baru request
     */
    public static boolean checkAndRequestCamera(Activity activity) {
        if (hasPermission(activity, Manifest.permission.CAMERA)) {
            return true;
        }
        requestCamera(activity);
        return false;
    }

    /**
     * Check Storage/Media, jika belum granted langsung request
     * @return true jika sudah granted, false jika baru request
     */
    public static boolean checkAndRequestStorage(Activity activity) {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ? Manifest.permission.READ_MEDIA_IMAGES
                : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (hasPermission(activity, permission)) {
            return true;
        }
        requestStorage(activity);
        return false;
    }

    /**
     * Check Audio, jika belum granted langsung request
     * @return true jika sudah granted, false jika baru request
     */
    public static boolean checkAndRequestAudio(Activity activity) {
        if (hasPermission(activity, Manifest.permission.RECORD_AUDIO)) {
            return true;
        }
        requestAudio(activity);
        return false;
    }

    /**
     * Check Notification, jika belum granted langsung request
     * @return true jika sudah granted, false jika baru request
     */
    public static boolean checkAndRequestNotification(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (hasPermission(activity, Manifest.permission.POST_NOTIFICATIONS)) {
                return true;
            }
            requestNotification(activity);
            return false;
        }
        return true; // Android < 13 tidak perlu permission
    }

    /**
     * Check Location, jika belum granted langsung request
     * @return true jika sudah granted, false jika baru request
     */
    public static boolean checkAndRequestLocation(Activity activity) {
        if (hasPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            return true;
        }
        requestLocation(activity);
        return false;
    }

    // ============================================
    // HANDLE RESULT
    // ============================================

    /**
     * Helper untuk handle onRequestPermissionsResult
     *
     * Usage di Activity/Fragment:
     *
     * @Override
     * public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
     *     super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     *
     *     if (PermissionUtil.isGranted(grantResults)) {
     *         // Permission granted - lanjutkan aksi
     *         switch (requestCode) {
     *             case PermissionUtil.REQUEST_CAMERA:
     *                 openCamera();
     *                 break;
     *             case PermissionUtil.REQUEST_STORAGE:
     *                 pickImage();
     *                 break;
     *         }
     *     } else {
     *         // Permission denied
     *         if (PermissionUtil.isPermanentlyDenied(this, permissions[0])) {
     *             // Show dialog to go to settings
     *             showGoToSettingsDialog();
     *         } else {
     *             // Show rationale
     *             Toast.makeText(this, "Permission diperlukan untuk fitur ini", Toast.LENGTH_SHORT).show();
     *         }
     *     }
     * }
     */
    public static boolean isGranted(int[] grantResults) {
        return grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Buka Settings untuk enable permission manual
     */
    public static void openSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    // ============================================
    // RATIONALE MESSAGES (opsional, bisa dipakai di DialogUtil)
    // ============================================

    /**
     * Get user-friendly message untuk permission rationale
     */
    public static String getRationaleMessage(String permission) {
        switch (permission) {
            case Manifest.permission.CAMERA:
                return "Kamera diperlukan untuk mengambil foto";

            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.READ_MEDIA_IMAGES:
                return "Akses penyimpanan diperlukan untuk memilih gambar";

            case Manifest.permission.RECORD_AUDIO:
                return "Mikrofon diperlukan untuk merekam audio";

            case Manifest.permission.POST_NOTIFICATIONS:
                return "Notifikasi diperlukan agar Anda tidak ketinggalan update";

            case Manifest.permission.ACCESS_COARSE_LOCATION:
                return "Lokasi diperlukan untuk fitur ini";

            default:
                return "Izin ini diperlukan untuk menggunakan fitur";
        }
    }

    /**
     * Get title untuk permission
     */
    public static String getPermissionTitle(String permission) {
        switch (permission) {
            case Manifest.permission.CAMERA:
                return "Izin Kamera";
            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.READ_MEDIA_IMAGES:
                return "Izin Penyimpanan";
            case Manifest.permission.RECORD_AUDIO:
                return "Izin Mikrofon";
            case Manifest.permission.POST_NOTIFICATIONS:
                return "Izin Notifikasi";
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                return "Izin Lokasi";
            default:
                return "Izin Aplikasi";
        }
    }
}