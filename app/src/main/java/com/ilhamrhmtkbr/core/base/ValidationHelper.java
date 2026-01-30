package com.ilhamrhmtkbr.core.base;

import android.content.Context;

/**
 * Static helper untuk akses Context di BaseValidation
 * Initialize sekali di Application class, terus tinggal pake aja
 */
public class ValidationHelper {
    private static Context appContext;

    /**
     * Initialize di Application class (WAJIB!)
     */
    public static void init(Context context) {
        appContext = context.getApplicationContext();
    }

    public static Context getContext() {
        if (appContext == null) {
            throw new IllegalStateException(
                    "ValidationHelper not initialized! " +
                            "Call ValidationHelper.init(context) in Application class"
            );
        }
        return appContext;
    }

    public static String getString(int resId) {
        return getContext().getString(resId);
    }

    public static String getString(int resId, Object... formatArgs) {
        return getContext().getString(resId, formatArgs);
    }
}