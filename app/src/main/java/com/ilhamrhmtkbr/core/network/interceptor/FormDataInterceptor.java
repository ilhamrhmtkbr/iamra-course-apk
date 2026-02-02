package com.ilhamrhmtkbr.core.network.interceptor;

import android.util.Log;

import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.core.network.CookieManager;
import com.ilhamrhmtkbr.core.utils.tools.LangUtil;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FormDataInterceptor implements Interceptor {
    private static final String TAG = "FormDataInterceptor";
    private CookieManager cookieManager;
    private LangUtil langUtil;

    @Inject
    public FormDataInterceptor(CookieManager cookieManager, LangUtil langUtil) {
        this.cookieManager = cookieManager;
        this.langUtil = langUtil;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        RequestBody body = original.body();

        boolean isMultipart = body instanceof MultipartBody;

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "=== REQUEST DEBUG ===");
            Log.d(TAG, "URL: " + original.url());
            Log.d(TAG, "Method: " + original.method());
            Log.d(TAG, "Body type: " + (body != null ? body.getClass().getSimpleName() : "null"));
            Log.d(TAG, "Is Multipart: " + isMultipart);
        }

        Request.Builder requestBuilder = original.newBuilder()
                .header("Accept", "application/json")
                .header("X-Client-Type", "android")
                .header("Accept-Language", langUtil.getLang());

        if (!isMultipart) {
            // Hanya tambahkan Content-Type JSON untuk NON-multipart
            requestBuilder.header("Content-Type", "application/json");
        }
        // Untuk multipart, JANGAN tambahkan header Content-Type
        // Biarkan OkHttp yang handle otomatis dengan boundary yang benar

        Request finalRequest = requestBuilder.build();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Final Headers: " + finalRequest.headers());
        }

        return chain.proceed(finalRequest);
    }
}