package com.ilhamrhmtkbr.presentation.guest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.guest.certificates.CertificatesFragment;
import com.ilhamrhmtkbr.presentation.guest.courses.CoursesFragment;
import com.ilhamrhmtkbr.presentation.guest.home.HomeFragment;
import com.ilhamrhmtkbr.core.utils.tools.FragmentUtil;

public interface SetupExtraListener extends CustomCondition {
    default void setupExtraListener(
            AppCompatActivity activity,
            NavigationView navigationView,
            ConstraintLayout navHome,
            ConstraintLayout navCourses,
            ConstraintLayout navCertificates
    ) {
        String navigateTo = activity.getIntent().getStringExtra("navigate_to");
        String certificateId = activity.getIntent().getStringExtra("certificate_id");

        Fragment initialFragment;

        if (navigateTo != null) {
            switch (navigateTo) {
                case "courses":
                    initialFragment = new CoursesFragment();
                    break;
                case "certificates":
                    CertificatesFragment certFragment = new CertificatesFragment();
                    if (certificateId != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("certificate_id", certificateId);
                        certFragment.setArguments(bundle);
                    }
                    initialFragment = certFragment;
                    break;
                case "home":
                default:
                    initialFragment = new HomeFragment();
                    break;
            }
        } else {
            initialFragment = new HomeFragment();
        }

        FragmentUtil.navigateTo(activity, initialFragment, R.id.fragment_content);
        updateSelectedNavByFragment(initialFragment, navigationView, navHome, navCourses, navCertificates);
    }
}
