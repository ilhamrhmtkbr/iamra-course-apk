package com.ilhamrhmtkbr.presentation.forum;

import android.content.Context;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.SessionRepository;
import com.ilhamrhmtkbr.presentation.instructor.InstructorActivity;
import com.ilhamrhmtkbr.presentation.member.MemberActivity;
import com.ilhamrhmtkbr.presentation.student.StudentActivity;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;

interface SetupBottomNav {
    /**
     * Di pake buat atur bottomnav ketika item di klik
     */
    default void setupBottomNavLayoutListener(
            BottomNavigationView bottomNavigationView,
            Context context,
            String role) {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_profile) {
                    if (role.equals(SessionRepository.INSTRUCTOR)) {
                        RedirectUtil.redirectToActivity(context, InstructorActivity.class);
                    } else {
                        RedirectUtil.redirectToActivity(context, StudentActivity.class);
                    }
                } else if (item.getItemId() == R.id.nav_courses) {
                    if (role.equals(SessionRepository.INSTRUCTOR)) {
                        RedirectUtil.redirectToActivityWithExtra(context, InstructorActivity.class, "navigate_to", "courses");
                    } else {
                        RedirectUtil.redirectToActivityWithExtra(context, StudentActivity.class, "navigate_to", "courses");
                    }
                } else if (item.getItemId() == R.id.nav_setting) {
                    RedirectUtil.redirectToActivity(context, MemberActivity.class);
                } else {
                    return false;
                }
                return true;
            }
        });
    }
}
