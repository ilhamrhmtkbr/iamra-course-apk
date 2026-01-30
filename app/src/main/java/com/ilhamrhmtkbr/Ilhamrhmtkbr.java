package com.ilhamrhmtkbr;

import android.app.Application;

import com.ilhamrhmtkbr.core.base.ValidationHelper;
import com.ilhamrhmtkbr.core.utils.ui.ThemeUtil;
import com.midtrans.sdk.uikit.api.model.CustomColorTheme;
import com.midtrans.sdk.uikit.external.UiKitApi;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class Ilhamrhmtkbr extends Application {
    public static UiKitApi midtransApi;

    @Override
    public void onCreate() {
        super.onCreate();

        midtransApi = new UiKitApi.Builder()
                .withContext(this)  // Application context
                .withMerchantClientKey(BuildConfig.MIDTRANS_CLIENT_KEY)
                .withMerchantUrl("https://api-student.course.iamra.site/v1/transactions/callback/") // set transaction finish callback (sdk callback)
                .enableLog(true) // Hanya untuk testing, matikan di production
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
    }
}
