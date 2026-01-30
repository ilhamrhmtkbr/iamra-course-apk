package com.ilhamrhmtkbr.presentation.guest;

import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.guest.courses.CoursesFragment;
import com.ilhamrhmtkbr.core.utils.tools.FragmentUtil;

public interface SetupSearchView {
    default void setupSearchView(
            AppCompatActivity activity,
            SearchView searchCourse
    ){
        searchCourse.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Fragment currentFragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_content);

                if (currentFragment instanceof CoursesFragment) {
                    ((CoursesFragment) currentFragment).performSearch(query);
                    searchCourse.clearFocus();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    default void setupSearchNavigateOverlay(
            AppCompatActivity activity,
            View searchNavigateToCourse
    ) {
        searchNavigateToCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentUtil.navigateTo(activity, new CoursesFragment(), R.id.fragment_content);
            }
        });
    }
}
