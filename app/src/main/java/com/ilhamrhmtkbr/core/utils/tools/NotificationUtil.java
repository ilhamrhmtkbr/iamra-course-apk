package com.ilhamrhmtkbr.core.utils.tools;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ilhamrhmtkbr.R;

public class NotificationUtil {
    public static final String CHANNEL_ASSIGNMENTS = "assignments";
    public static final String CHANNEL_SYSTEM = "system";
    public static final int NOTIFICATION_ASSIGNMENT_DUE = 1002;

    private static NotificationUtil instance;
    private Context context;
    private NotificationManagerCompat notificationManager;

    private NotificationUtil(Context context) {
        this.context = context.getApplicationContext();
        this.notificationManager = NotificationManagerCompat.from(this.context);
        createNotificationChannels();
    }

    public static synchronized NotificationUtil getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationUtil(context);
        }
        return instance;
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = context.getSystemService(NotificationManager.class);

            // Assignments Channel
            NotificationChannel assignmentChannel = new NotificationChannel(
                    CHANNEL_ASSIGNMENTS,
                    "Request & Response",
                    NotificationManager.IMPORTANCE_HIGH
            );
            assignmentChannel.setDescription("Notifikasi request dan response");
            assignmentChannel.enableLights(true);
            assignmentChannel.enableVibration(true);
            assignmentChannel.setShowBadge(true);

            // System Channel
            NotificationChannel systemChannel = new NotificationChannel(
                    CHANNEL_SYSTEM,
                    "Sistem",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            systemChannel.setDescription("Notifikasi sistem dan update aplikasi");
            systemChannel.enableLights(true);
            systemChannel.enableVibration(true);
            systemChannel.setShowBadge(true);

            // Register channels
            manager.createNotificationChannel(assignmentChannel);
            manager.createNotificationChannel(systemChannel);
        }
    }

    public void showCustomNotification(String channelId, int notificationId, String title,
                                       String message, Intent actionIntent, String actionText) {
        PendingIntent pendingIntent = null;
        if (actionIntent != null) {
            pendingIntent = PendingIntent.getActivity(context, 0, actionIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_custom_warning)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setColor(context.getResources().getColor(R.color.blue_color));

        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
            if (actionText != null && !actionText.isEmpty()) {
                builder.addAction(R.drawable.ic_custom_click, actionText, pendingIntent);
            }
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }

    public void showHeadsUpNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ASSIGNMENTS) // Pake channel HIGH importance
                .setSmallIcon(R.drawable.ic_custom_warning)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_custom_warning))
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // HIGH priority!
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setDefaults(NotificationCompat.DEFAULT_ALL) // Sound & vibration
                .setAutoCancel(true)
                .setColor(context.getResources().getColor(R.color.blue_color));

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(NOTIFICATION_ASSIGNMENT_DUE, builder.build());
    }
}