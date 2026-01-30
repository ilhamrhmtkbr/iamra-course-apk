package com.ilhamrhmtkbr.presentation.onboarding;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.ilhamrhmtkbr.core.base.BaseProtectedActivity;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;
import com.ilhamrhmtkbr.databinding.ActivityOnboardingBinding;
import com.ilhamrhmtkbr.core.network.NetworkManager;
import com.ilhamrhmtkbr.presentation.guest.GuestActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OnboardingActivity extends BaseProtectedActivity implements SetupAppFlow, SetupPrefs {
    private ActivityOnboardingBinding binding;
    private int currentPosition = 0;
    @Inject
    NetworkManager networkManager;

    @Override
    protected String getRequiredRole() {
        return "onboarding";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnboardingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("OnboardingActivity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        networkManager.startNetworkMonitoring();

        if (isOnboardingCompleted(sharedPreferences)) {
            RedirectUtil.redirectToActivity(this, GuestActivity.class);
        } else {
            setOnboardingCompletedTrue(editor);
            setupViewPager(this, binding.viewPager, binding.btnNext, binding.btnPrevious, binding.btnGetStarted, binding.progressIndicator, binding.tvPageCounter);
            setupClickListeners(binding.btnNext, binding.btnPrevious, binding.btnGetStarted, binding.viewPager);
        }
    }

    @Override
    public void onBackPressed() {
        if (currentPosition > 0) {
            binding.viewPager.setCurrentItem(currentPosition - 1, true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public void setCurrentPosition(int number) {
        this.currentPosition = number;
    }
}