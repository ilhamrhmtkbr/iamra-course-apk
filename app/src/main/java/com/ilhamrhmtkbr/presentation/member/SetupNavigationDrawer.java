package com.ilhamrhmtkbr.presentation.member;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.auth.SessionRepository;
import com.ilhamrhmtkbr.presentation.instructor.InstructorActivity;
import com.ilhamrhmtkbr.presentation.guest.GuestActivity;
import com.ilhamrhmtkbr.presentation.student.StudentActivity;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;

interface SetupNavigationDrawer {
    /**
     * Di pake buat atur navigation menu drawer ketika item di klik
     */
    default void setupNavigationDrawerListener(
            View view,
            AuthStateManager authManager,
            NavigationView navigationView,
            NavController navController,
            Context context,
            DrawerLayout drawerLayout
   ) {
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_private) {
                    if (authManager.getCurrentUserEmailVerifiedAt().isEmpty()) {
                        DialogUtil.showErrorSnackbar(view, "Please Verify Email First");
                        return false;
                    }

                    String currentRole = authManager.getCurrentUserRole();
                    if (currentRole != null) {
                        if (currentRole.equals(SessionRepository.INSTRUCTOR)) {
                            RedirectUtil.redirectToActivity(context, InstructorActivity.class);
                        } else if (currentRole.equals(SessionRepository.STUDENT)) {
                            RedirectUtil.redirectToActivity(context, StudentActivity.class);
                        } else {
                            Toast.makeText(context, "Please register your role first", Toast.LENGTH_LONG).show();
                            navController.navigate(R.id.nav_role_register);
                        }
                    } else {
                        Toast.makeText(context, "Please register your role first", Toast.LENGTH_LONG).show();
                        navController.navigate(R.id.nav_role_register);
                    }
                    return true;
                }

                if (id == R.id.nav_logout) {
                    authManager.logout();
                    RedirectUtil.redirectToActivity(context, GuestActivity.class);
                    return true;
                }

                if (id == R.id.nav_role_register) {
                    if (authManager.getCurrentUserEmailVerifiedAt() == null) {
                        DialogUtil.showErrorSnackbar(view, "Please Verify Email");
                        return false;
                    }
                }

                boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

                if (handled) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return handled;
            }
        });
    }
}
