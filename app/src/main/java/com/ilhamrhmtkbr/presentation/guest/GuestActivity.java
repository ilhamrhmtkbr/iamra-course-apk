package com.ilhamrhmtkbr.presentation.guest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.base.BaseProtectedActivity;
import com.ilhamrhmtkbr.core.utils.tools.PermissionUtil;
import com.ilhamrhmtkbr.presentation.qrscanner.QrScannerActivity;
import com.ilhamrhmtkbr.databinding.ActivityPublicBinding;
import com.ilhamrhmtkbr.core.utils.ui.LayoutUtil;
import com.ilhamrhmtkbr.core.utils.system.NavigationAndroidBarUtil;
import com.ilhamrhmtkbr.core.utils.system.StatusBarUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GuestActivity extends BaseProtectedActivity
        implements SetupFragmentManager, CustomCondition,
        SetupBottomNav, SetupNavigationDrawer, SetupOptionsMenu,
        SetupExtraListener, SetupSearchView {
    private ActivityPublicBinding binding;
    private View selectedNavItem;

    @Override
    public String getRequiredRole() {
        return "GuestActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        binding = ActivityPublicBinding.inflate(getLayoutInflater());
        LayoutUtil.setupLayoutFullscreen(this);
        StatusBarUtil.setupStatusBarBackgroundColorTransparent(this);
        NavigationAndroidBarUtil.setupNavigationAndroidBarBackgroundColor(this, R.color.transparent);
        setContentView(binding.getRoot());

        setupFragmentManager(this, binding.navHome, binding.navCourses, binding.navCertificates, binding.navView, binding.searchNavigateToCourse, binding.searchCourse);
        setupBottomNavLayoutListener(this, binding.navHome, binding.navCourses, binding.navCertificates, binding.navProfile, binding.navView, binding.searchNavigateToCourse, binding.searchCourse);
        setupNavigationDrawerListener(this, binding.buttonToggleNavigation, binding.drawerLayout, binding.navView);
        setupOptionsMenu(this, binding.buttonToggleOverflow);
        setupQRScanner();
        setupExtraListener(this, binding.navView, binding.navHome, binding.navCourses, binding.navCertificates);
        setupSearchView(this, binding.searchCourse);
        setupSearchNavigateOverlay(this, binding.searchNavigateToCourse);
    }

    private void setupQRScanner() {
        binding.buttonScanCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionUtil.checkAndRequestCamera(GuestActivity.this)) {
                    Intent intent = new Intent(GuestActivity.this, QrScannerActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void setSelectedNavItem(View view) {
        this.selectedNavItem = view;
    }

    @Override
    public View getSelectedNavView() {
        return selectedNavItem;
    }
}