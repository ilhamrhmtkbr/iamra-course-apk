package com.ilhamrhmtkbr.presentation.forum;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.auth.SessionRepository;
import com.ilhamrhmtkbr.presentation.instructor.InstructorActivity;
import com.ilhamrhmtkbr.presentation.guest.GuestActivity;
import com.ilhamrhmtkbr.core.base.BaseProtectedActivity;
import com.ilhamrhmtkbr.presentation.student.StudentActivity;
import com.ilhamrhmtkbr.databinding.ActivityForumBinding;
import com.ilhamrhmtkbr.core.utils.system.NavigationAndroidBarUtil;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;
import com.ilhamrhmtkbr.core.utils.system.StatusBarUtil;
import com.ilhamrhmtkbr.core.utils.system.ToolbarUtil;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ForumActivity extends BaseProtectedActivity
        implements SetupBottomNav, SetupNavigationDrawer {
    private Toolbar toolbar;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected String getRequiredRole() {
        return SessionRepository.FORUM;
    }

    @Inject
    public AuthStateManager authStateManager;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityForumBinding binding = ActivityForumBinding.inflate(getLayoutInflater());
        StatusBarUtil.setupStatusBarBackgroundColorTransparent(this);
        NavigationAndroidBarUtil.setupNavigationAndroidBarBackgroundColor(this, R.color.wa_bg_color);
        NavigationAndroidBarUtil.setupNavigationAndroidBarIconColor(this);
        setContentView(binding.getRoot());

        if (authStateManager.getCurrentUserRole().equals(SessionRepository.INSTRUCTOR)) {
            RedirectUtil.afterOnBackPressed(this, InstructorActivity.class);
        } else {
            RedirectUtil.afterOnBackPressed(this, StudentActivity.class);
        }

        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        ToolbarUtil.setupToolbarNavigationIconColor(this, binding.toolbar, R.color.wa_white_color);

        NavController navController = Navigation.findNavController(this, R.id.fragment_content);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_chat_group,
                R.id.nav_chat_message)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        setupNavigationDrawerListener(binding.navView, navController, this, authStateManager.getCurrentUserRole(), binding.drawerLayout);
        setupBottomNavLayoutListener(binding.bottomNavView, this, authStateManager.getCurrentUserRole());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.popup_forum, menu);
        ToolbarUtil.setupToolbarOverflowIconColor(this, toolbar, R.color.wa_white_color);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            RedirectUtil.redirectToActivityWithExtra(this, GuestActivity.class, "navigate_to", "home");
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
}