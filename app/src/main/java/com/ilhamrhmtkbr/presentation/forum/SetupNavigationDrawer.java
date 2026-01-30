package com.ilhamrhmtkbr.presentation.forum;

import android.content.Context;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.SessionRepository;
import com.ilhamrhmtkbr.presentation.instructor.InstructorActivity;
import com.ilhamrhmtkbr.presentation.member.MemberActivity;
import com.ilhamrhmtkbr.presentation.student.StudentActivity;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;

interface SetupNavigationDrawer {
    /**
     * Di pake buat atur navigation menu drawer ketika item di klik
     */
    default void setupNavigationDrawerListener(
            NavigationView navigationView,
            NavController navController,
            Context context,
            String role,
            DrawerLayout drawerLayout
   ) {
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_setting) {
                    RedirectUtil.redirectToActivity(context, MemberActivity.class);
                } else if (id == R.id.nav_profile) {
                    if (role.equals(SessionRepository.STUDENT)) {
                        RedirectUtil.redirectToActivity(context, StudentActivity.class);
                    } else if (role.equals(SessionRepository.INSTRUCTOR)) {
                        RedirectUtil.redirectToActivity(context, InstructorActivity.class);
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
                    if (!handled) return false;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}
