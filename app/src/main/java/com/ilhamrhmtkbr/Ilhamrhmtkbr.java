package com.ilhamrhmtkbr;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ilhamrhmtkbr.BuildConfig;

import com.google.android.recaptcha.Recaptcha;
import com.google.android.recaptcha.RecaptchaTasksClient;
import com.ilhamrhmtkbr.core.base.ValidationHelper;
import com.ilhamrhmtkbr.core.utils.ui.ThemeUtil;
import com.midtrans.sdk.uikit.api.model.CustomColorTheme;
import com.midtrans.sdk.uikit.external.UiKitApi;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class Ilhamrhmtkbr extends Application {
    public static UiKitApi midtransApi;

    // Static agar bisa diakses dari mana saja
    @Nullable
    private static RecaptchaTasksClient recaptchaTasksClient = null;

    @Override
    public void onCreate() {
        super.onCreate();

        midtransApi = new UiKitApi.Builder()
                .withContext(this)
                .withMerchantClientKey(BuildConfig.MIDTRANS_CLIENT_KEY)
                .withMerchantUrl("https://api-student.course.iamra.site/v1/transactions/callback/")
                .enableLog(true)
                .withColorTheme(new CustomColorTheme(
                        "#09090b",
                        "#09090b",
                        "#5f6368"
                ))
                .build();

        // Set Theme
        ThemeUtil themeUtil = new ThemeUtil(this);
        themeUtil.applySavedTheme();

        ValidationHelper.init(this);
        initializeRecaptchaClient();
    }

    private void initializeRecaptchaClient() {
        Recaptcha.getTasksClient(this, BuildConfig.GOOGLE_RECAPTCHA_SITE_KEY)
                .addOnSuccessListener(client -> {
                    Ilhamrhmtkbr.recaptchaTasksClient = client;
                })
                .addOnFailureListener(e -> {
                    Log.e("App", "Failed to initialize reCAPTCHA");
                });
    }

    /**
     * Method untuk mendapatkan reCAPTCHA client
     */
    @Nullable
    public static RecaptchaTasksClient getRecaptchaClient() {
        return recaptchaTasksClient;
    }
}