package com.ilhamrhmtkbr.presentation.guest;

import android.view.Gravity;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.guest.certificates.CertificatesFragment;
import com.ilhamrhmtkbr.presentation.guest.courses.CoursesFragment;
import com.ilhamrhmtkbr.presentation.guest.home.HomeFragment;
import com.ilhamrhmtkbr.core.utils.tools.FragmentUtil;

public interface SetupOptionsMenu {
    default void setupOptionsMenu(
            AppCompatActivity activity,
            ImageView buttonToggleOverflow
    ) {
        buttonToggleOverflow.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(activity, v, Gravity.END
            );

            popupMenu.getMenuInflater().inflate(
                    R.menu.popup_public,
                    popupMenu.getMenu()
            );

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    FragmentUtil.navigateTo(activity, new HomeFragment(), R.id.fragment_content);
                } else if (id == R.id.nav_courses) {
                    FragmentUtil.navigateTo(activity, new CoursesFragment(), R.id.fragment_content);
                } else if (id == R.id.nav_certificates) {
                    FragmentUtil.navigateTo(activity, new CertificatesFragment(), R.id.fragment_content);
                }
                return false;
            });

            popupMenu.show();
        });
    }

}
