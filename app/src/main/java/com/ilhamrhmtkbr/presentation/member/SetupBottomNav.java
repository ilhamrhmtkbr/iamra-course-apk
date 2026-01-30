package com.ilhamrhmtkbr.presentation.member;

import android.content.Context;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.SessionRepository;
import com.ilhamrhmtkbr.presentation.forum.ForumActivity;
import com.ilhamrhmtkbr.presentation.instructor.InstructorActivity;
import com.ilhamrhmtkbr.presentation.guest.GuestActivity;
import com.ilhamrhmtkbr.presentation.student.StudentActivity;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;

interface SetupBottomNav {
    default void setupBottomNavLayoutListener(
            BottomNavigationView bottomNavigationView,
            Context context,
            String role,
            NavController navController
    ) {
        bottomNavigationView.setSelectedItemId(R.id.nav_additional_info);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_public) {
                RedirectUtil.redirectToActivity(context, GuestActivity.class);
            } else if (id == R.id.nav_courses) {
                RedirectUtil.redirectToActivityWithExtra(context, GuestActivity.class, "navigate_to", "courses");
            } else if (id == R.id.nav_forum) {
                RedirectUtil.redirectToActivity(context, ForumActivity.class);
            } else if (id == R.id.nav_profile) {
                if (SessionRepository.INSTRUCTOR.equals(role)) {
                    RedirectUtil.redirectToActivityWithExtra(context, InstructorActivity.class, "navigate_to", "profile");
                } else if (SessionRepository.STUDENT.equals(role)) {
                    RedirectUtil.redirectToActivityWithExtra(context, StudentActivity.class, "navigate_to", "profile");
                } else {
                    Toast.makeText(context, "Please register your role first", Toast.LENGTH_LONG).show();
                    navController.navigate(R.id.nav_role_register);
                }
            } else {
                return NavigationUI.onNavDestinationSelected(item, navController);
            }

            return true;
        });
    }
}
