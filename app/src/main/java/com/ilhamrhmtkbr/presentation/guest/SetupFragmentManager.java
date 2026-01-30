package com.ilhamrhmtkbr.presentation.guest;

import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.ilhamrhmtkbr.R;

public interface SetupFragmentManager extends CustomCondition {
    default void setupFragmentManager(
            AppCompatActivity activity,
            ConstraintLayout navHome,
            ConstraintLayout navCourses,
            ConstraintLayout navCertificates,
            NavigationView navigationView,
            View searchNavigateToCourse,
            SearchView searchCourse
    ) {
        activity.getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Fragment currentFragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_content);
            if (currentFragment != null) {
                updateSelectedNavByFragment(currentFragment, navigationView, navHome, navCourses, navCertificates);
                updateSearchViewOverlay(currentFragment, searchNavigateToCourse, searchCourse);
            }
        });
    }
}
