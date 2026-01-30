package com.ilhamrhmtkbr.presentation.student;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.SessionRepository;
import com.ilhamrhmtkbr.presentation.member.MemberActivity;
import com.ilhamrhmtkbr.core.base.BaseProtectedActivity;
import com.ilhamrhmtkbr.presentation.guest.GuestActivity;
import com.ilhamrhmtkbr.databinding.ActivityStudentBinding;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;
import com.ilhamrhmtkbr.core.utils.system.StatusBarUtil;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StudentActivity extends BaseProtectedActivity
        implements SetupBottomNav, SetupNavigationDrawer, SetupExtraListener {
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected String getRequiredRole() {
        return SessionRepository.STUDENT;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        ActivityStudentBinding binding = ActivityStudentBinding.inflate(getLayoutInflater());
        StatusBarUtil.setupStatusBarIconColorWithConditionalTheme(this);
        StatusBarUtil.setupStatusBarBackgroundColorTransparent(this);
        setContentView(binding.getRoot());
        RedirectUtil.afterOnBackPressed(this, MemberActivity.class);
        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.fragment_content);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile,
                R.id.nav_cart,
                R.id.nav_courses,
                R.id.nav_certificates,
                R.id.nav_progresses,
                R.id.nav_questions,
                R.id.nav_reviews,
                R.id.nav_transactions,
                R.id.nav_setting,
                R.id.nav_forum)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        setupNavigationDrawerListener(binding.navView, navController, this, binding.drawerLayout);
        setupBottomNavLayoutListener(binding.bottomNavView, this);
        setupExtraListener(this, navController);

        if (getIntent().hasExtra("navigate_to") && Objects.equals(getIntent().getStringExtra("navigate_to"), "carts")) {
            navController.navigate(R.id.nav_cart);
        }
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
