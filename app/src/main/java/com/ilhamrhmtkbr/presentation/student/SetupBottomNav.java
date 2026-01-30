package com.ilhamrhmtkbr.presentation.student;

import android.content.Context;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.forum.ForumActivity;
import com.ilhamrhmtkbr.presentation.guest.GuestActivity;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;

interface SetupBottomNav {
    /**
     * Di pake buat atur bottomnav ketika item di klik
     */
    default void setupBottomNavLayoutListener(BottomNavigationView bottomNavigationView, Context context) {
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_public) {
                    RedirectUtil.redirectToActivity(context, GuestActivity.class);
                } else if (item.getItemId() == R.id.nav_courses) {
                    RedirectUtil.redirectToActivityWithExtra(context, GuestActivity.class, "navigate_to", "courses");
                } else if (item.getItemId() == R.id.nav_forum) {
                    RedirectUtil.redirectToActivity(context, ForumActivity.class);
                } else {
                    return false;
                }
                return true;
            }
        });
    }
}
