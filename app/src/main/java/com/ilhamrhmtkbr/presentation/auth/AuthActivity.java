package com.ilhamrhmtkbr.presentation.auth;

import android.os.Bundle;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.SessionRepository;
import com.ilhamrhmtkbr.core.base.BaseProtectedActivity;
import com.ilhamrhmtkbr.presentation.guest.GuestActivity;
import com.ilhamrhmtkbr.databinding.ActivityUserAuthBinding;
import com.ilhamrhmtkbr.presentation.auth.login.LoginFragment;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AuthActivity extends BaseProtectedActivity {
    @Override
    protected String getRequiredRole() {
        return SessionRepository.GUEST;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        com.ilhamrhmtkbr.databinding.ActivityUserAuthBinding binding = ActivityUserAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RedirectUtil.afterOnBackPressed(this, GuestActivity.class);
        if (bundle == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_content, new LoginFragment())
                    .commit();
        }
    }
}