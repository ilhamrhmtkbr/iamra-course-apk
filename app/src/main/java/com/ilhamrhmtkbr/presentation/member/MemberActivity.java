package com.ilhamrhmtkbr.presentation.member;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.SessionRepository;
import com.ilhamrhmtkbr.core.base.BaseProtectedActivity;
import com.ilhamrhmtkbr.presentation.guest.GuestActivity;
import com.ilhamrhmtkbr.databinding.ActivityUserMemberBinding;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MemberActivity extends BaseProtectedActivity
        implements SetupNotification, SetupBottomNav, SetupNavigationDrawer,
        SetupToolbar, SetupExtraListener, SetupUserData {
    private AppBarConfiguration appBarConfiguration;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private int retryCount = 0;
    private boolean isBottomNavVisible = true;

    @Override
    protected String getRequiredRole() {
        return SessionRepository.MEMBER;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityUserMemberBinding binding = ActivityUserMemberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupNotification(this);
        RedirectUtil.afterOnBackPressed(this, GuestActivity.class);

        NavController navController = Navigation.findNavController(this, R.id.fragment_content);
        setupCollapsingToolbar(this, binding.toolbar, binding.collapsingToolbar, binding.appBar, navController);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_additional_info,
                R.id.nav_authentication,
                R.id.nav_email,
                R.id.nav_role_register,
                R.id.nav_logout)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        setupBottomNavLayoutListener(binding.bottomNavView, this, authStateManager.getCurrentUserRole(), navController);
        setupNavigationDrawerListener(binding.getRoot(), authStateManager, binding.navView, navController, this, binding.drawerLayout);
        setupScrollListener(binding.nestedScrollView, binding.bottomNavView);
        setupUserData(authStateManager, handler, binding.navView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            RedirectUtil.redirectToActivity(this, GuestActivity.class);
            return true;
        } else if (id == R.id.nav_courses) {
            RedirectUtil.redirectToActivityWithExtra(this, GuestActivity.class, "navigate_to", "courses");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragment_content);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean getConditionIsBottomNavVisible() {
        return this.isBottomNavVisible;
    }

    @Override
    public void setIsBottomNavVisible(boolean condition) {
        this.isBottomNavVisible = condition;
    }

    @Override
    public int getRetryCount() {
        return this.retryCount;
    }

    @Override
    public void setRetryCount(int number) {
        this.retryCount = number;
    }
}