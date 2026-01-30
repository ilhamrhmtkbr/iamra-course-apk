package com.ilhamrhmtkbr.core.base;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.auth.SessionRepository;
import com.ilhamrhmtkbr.core.utils.tools.LangUtil;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;
import com.ilhamrhmtkbr.presentation.auth.AuthActivity;
import com.ilhamrhmtkbr.presentation.member.MemberActivity;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public abstract class BaseProtectedActivity extends AppCompatActivity {
    @Inject
    protected AuthStateManager authStateManager;

    protected abstract String getRequiredRole();

    @Inject
    LangUtil langUtil;

    @Override
    protected void attachBaseContext(Context newBase) {
        String languageCode = newBase.getSharedPreferences("lang_prefs", Context.MODE_PRIVATE)
                .getString("lang_mode", "en");

        // Apply locale
        Context context = updateLocale(newBase, languageCode);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }

        autoCheck();
    }

    private Context updateLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            return context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            return context;
        }
    }

    public void autoCheck() {
        String role = getRequiredRole();

        switch (role) {
            case SessionRepository.GUEST:
                if (authStateManager.isLoggedIn()) {
                    RedirectUtil.redirectToActivity(this, MemberActivity.class);
                }
                break;
            case "GuestActivity":
                break;
            case SessionRepository.FORUM:
                if (authStateManager.getCurrentUserRole().equals(SessionRepository.USER)) {
                    RedirectUtil.redirectToActivity(this, MemberActivity.class);
                }
                break;
            case SessionRepository.INSTRUCTOR:
                if (!authStateManager.getCurrentUserRole().equals(SessionRepository.INSTRUCTOR)) {
                    RedirectUtil.redirectToActivity(this, MemberActivity.class);
                }
                break;
            case SessionRepository.STUDENT:
                if (!authStateManager.getCurrentUserRole().equals(SessionRepository.STUDENT)) {
                    RedirectUtil.redirectToActivity(this, MemberActivity.class);
                }
                break;
            case SessionRepository.MEMBER:
                if (!authStateManager.isLoggedIn()) {
                    RedirectUtil.redirectToActivity(this, AuthActivity.class);
                }
                break;
        }
    }
}
