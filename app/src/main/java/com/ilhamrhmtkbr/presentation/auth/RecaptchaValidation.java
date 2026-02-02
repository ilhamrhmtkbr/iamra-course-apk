package com.ilhamrhmtkbr.presentation.auth;

import android.app.Activity;
import android.util.Log;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.recaptcha.RecaptchaAction;
import com.google.android.recaptcha.RecaptchaTasksClient;
import com.ilhamrhmtkbr.Ilhamrhmtkbr;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

public interface RecaptchaValidation {
    String TAG = "RecaptchaValidation";

    /**
     * Callback interface untuk handle async reCAPTCHA result
     */
    interface RecaptchaCallback {
        void onSuccess(String token);
        void onFailure(String errorMessage);
    }

    /**
     * Verify reCAPTCHA dan return token via callback
     *
     * @param activity Activity context
     * @param loading Loading layout untuk show/hide loading
     * @param callback Callback untuk handle success/failure
     */
    default void verifyRecaptcha(Activity activity, ConstraintLayout loading, RecaptchaCallback callback) {
        LoadingUtil.setup(true, loading);

        RecaptchaTasksClient client = Ilhamrhmtkbr.getRecaptchaClient();

        if (client == null) {
            LoadingUtil.setup(false, loading);
            Log.e(TAG, "reCAPTCHA client not initialized");
            callback.onFailure("reCAPTCHA not initialized. Please restart the app.");
            return;
        }

        client.executeTask(RecaptchaAction.LOGIN)
                .addOnSuccessListener(activity, token -> {
                    LoadingUtil.setup(false, loading);
                    Log.d(TAG, "reCAPTCHA verification successful");
                    callback.onSuccess(token);
                })
                .addOnFailureListener(activity, e -> {
                    LoadingUtil.setup(false, loading);
                    Log.e(TAG, "reCAPTCHA verification failed: " + e.getMessage());
                    callback.onFailure("reCAPTCHA verification failed: " + e.getMessage());
                });
    }
}