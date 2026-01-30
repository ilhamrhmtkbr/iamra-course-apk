package com.ilhamrhmtkbr.presentation.guest;

import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.guest.certificates.CertificatesFragment;
import com.ilhamrhmtkbr.presentation.guest.courses.CoursesFragment;
import com.ilhamrhmtkbr.presentation.guest.home.HomeFragment;
import com.ilhamrhmtkbr.core.utils.tools.FragmentUtil;

interface SetupNavigationDrawer {
    /**
     * Di pake buat atur navigation menu drawer ketika item di klik
     */
    default void setupNavigationDrawerListener(
            AppCompatActivity activity,
            ImageView buttonToggleNavigation,
            DrawerLayout drawerLayout,
            NavigationView navigationView
   ) {
        buttonToggleNavigation.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = null;

                if (id == R.id.nav_home) {
                    fragment = new HomeFragment();
                } else if (id == R.id.nav_courses) {
                    fragment = new CoursesFragment();
                } else if (id == R.id.nav_certificates) {
                    fragment = new CertificatesFragment();
                }

                if (fragment != null) {
                    FragmentUtil.navigateTo(activity, fragment, R.id.fragment_content);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}
