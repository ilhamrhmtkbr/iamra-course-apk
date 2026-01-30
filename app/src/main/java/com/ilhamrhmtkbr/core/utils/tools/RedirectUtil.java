package com.ilhamrhmtkbr.core.utils.tools;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class RedirectUtil {
    public static void redirectToActivity(Context from, Class<?> destiny) {
        Intent intent = new Intent(from, destiny);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        from.startActivity(intent);
    }

    public static void redirectToActivityWithExtra(Context from, Class<?> destiny, String extraName, String extraValue) {
        Intent intent = new Intent(from, destiny);
        intent.putExtra(extraName, extraValue);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        from.startActivity(intent);
    }

    public static void redirectToActivityWithBundle(Context from, Class<?> destiny, Bundle extras) {
        Intent intent = new Intent(from, destiny);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        from.startActivity(intent);
    }

    public static void afterOnBackPressed(AppCompatActivity activity, Class<?> destiny) {
        activity.getOnBackPressedDispatcher().addCallback(activity, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                redirectToActivity(activity, destiny);
            }
        });

    }
}
