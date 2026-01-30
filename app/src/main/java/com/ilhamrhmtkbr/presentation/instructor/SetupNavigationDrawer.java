package com.ilhamrhmtkbr.presentation.instructor;

import android.content.Context;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.forum.ForumActivity;
import com.ilhamrhmtkbr.presentation.member.MemberActivity;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;

interface SetupNavigationDrawer {
    /**
     * Di pake buat atur navigation menu drawer ketika item di klik
     */
    default void setupNavigationDrawerListener(
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
                if (id == R.id.nav_setting) {
                    RedirectUtil.redirectToActivity(context, MemberActivity.class);
                } else if (id == R.id.nav_forum) {
                    RedirectUtil.redirectToActivity(context, ForumActivity.class);
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
