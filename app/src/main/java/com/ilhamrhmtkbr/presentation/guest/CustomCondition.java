package com.ilhamrhmtkbr.presentation.guest;

import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.guest.certificates.CertificatesFragment;
import com.ilhamrhmtkbr.presentation.guest.courses.CoursesFragment;
import com.ilhamrhmtkbr.presentation.guest.home.HomeFragment;

public interface CustomCondition {
    default void updateSelectedNavByFragment(
            Fragment fragment,
            NavigationView navigationView,
            ConstraintLayout navHome,
            ConstraintLayout navCourses,
            ConstraintLayout navCertificates) {
        // Update bottom nav
        if (fragment instanceof HomeFragment) {
            selectNavItem(navHome);
            navigationView.setCheckedItem(R.id.nav_home);
        } else if (fragment instanceof CoursesFragment) {
            selectNavItem(navCourses);
            navigationView.setCheckedItem(R.id.nav_courses);
        } else if (fragment instanceof CertificatesFragment) {
            selectNavItem(navCertificates);
            navigationView.setCheckedItem(R.id.nav_certificates);
        }
    }

    default void selectNavItem(View newSelectedItem) {
        if (getSelectedNavView() != null) {
            getSelectedNavView().setSelected(false);
            updateIconAndTextState(getSelectedNavView(), false);
        }

        newSelectedItem.setSelected(true);
        updateIconAndTextState(newSelectedItem, true);
        setSelectedNavItem(newSelectedItem);
    }

    default void updateIconAndTextState(View parent, boolean selected) {
        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                child.setSelected(selected);
            }
        }
    }

    default void updateSearchViewOverlay(Fragment fragment, View searchNavigateToCourse, SearchView searchCourse) {
        if (fragment instanceof CoursesFragment) {
            searchNavigateToCourse.setVisibility(View.GONE);
        } else {
            searchNavigateToCourse.setVisibility(View.VISIBLE);
            searchCourse.setQuery("", false);
        }
    }

    void setSelectedNavItem(View view);
    View getSelectedNavView();
}
