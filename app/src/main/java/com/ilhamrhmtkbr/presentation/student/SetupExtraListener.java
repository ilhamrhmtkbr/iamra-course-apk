package com.ilhamrhmtkbr.presentation.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import com.ilhamrhmtkbr.R;

public interface SetupExtraListener {
    default void setupExtraListener(AppCompatActivity activity, NavController navController) {
        String navigateTo = activity.getIntent().getStringExtra("navigate_to");

        if (navigateTo != null) {
            if (navigateTo.equals("courses")) {
                navController.navigate(R.id.nav_courses); // Pakai ID yang ada di nav_graph
            } else {
                navController.navigate(R.id.nav_profile);
            }
        }
    }
}
