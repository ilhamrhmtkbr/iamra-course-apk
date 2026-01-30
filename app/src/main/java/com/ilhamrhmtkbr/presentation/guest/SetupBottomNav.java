package com.ilhamrhmtkbr.presentation.guest;

import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.navigation.NavigationView;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.member.MemberActivity;
import com.ilhamrhmtkbr.presentation.guest.certificates.CertificatesFragment;
import com.ilhamrhmtkbr.presentation.guest.courses.CoursesFragment;
import com.ilhamrhmtkbr.presentation.guest.home.HomeFragment;
import com.ilhamrhmtkbr.core.utils.tools.FragmentUtil;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;

interface SetupBottomNav extends CustomCondition {
    default void setupBottomNavLayoutListener(
            AppCompatActivity activity,
            ConstraintLayout navHome,
            ConstraintLayout navCourses,
            ConstraintLayout navCertificates,
            ConstraintLayout navProfile,
            NavigationView navigationView,
            View searchNavigateToCourse,
            SearchView searchCourse
    ) {
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSelectedNavByFragment(new HomeFragment(), navigationView, navHome, navCourses, navCertificates);
                updateSearchViewOverlay(new HomeFragment(), searchNavigateToCourse, searchCourse);
                FragmentUtil.navigateTo(activity, new HomeFragment(), R.id.fragment_content);
            }
        });
        navCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSelectedNavByFragment(new CoursesFragment(), navigationView, navHome, navCourses, navCertificates);
                updateSearchViewOverlay(new CoursesFragment(), searchNavigateToCourse, searchCourse);
                FragmentUtil.navigateTo(activity, new CoursesFragment(), R.id.fragment_content);
            }
        });
        navCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSelectedNavByFragment(new CertificatesFragment(), navigationView, navHome, navCourses, navCertificates);
                updateSearchViewOverlay(new CertificatesFragment(), searchNavigateToCourse, searchCourse);
                FragmentUtil.navigateTo(activity, new CertificatesFragment(), R.id.fragment_content);
            }
        });
        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectUtil.redirectToActivity(activity, MemberActivity.class);
            }
        });
    }
}
