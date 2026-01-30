package com.ilhamrhmtkbr.presentation.member;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.utils.ui.LayoutUtil;
import com.ilhamrhmtkbr.core.utils.system.StatusBarUtil;
import com.ilhamrhmtkbr.core.utils.ui.ThemeUtil;

public interface SetupToolbar {
    default void setupCollapsingToolbar(
            AppCompatActivity activity,
            Toolbar toolbar,
            CollapsingToolbarLayout collapsingToolbarLayout,
            AppBarLayout appBarLayout,
            NavController navController) {
        activity.setSupportActionBar(toolbar);

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                setToolbarColorExpanded(activity, collapsingToolbarLayout, toolbar);
            }
        });

        collapsingToolbarLayout.setTitleEnabled(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isCollapsed = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                // COLLAPSED
                if (scrollRange + verticalOffset == 0) {
                    if (!isCollapsed) {
                        isCollapsed = true;
                        setToolbarColorCollapsed(activity, collapsingToolbarLayout, toolbar);
                    }
                }
                // EXPANDED
                else if (isCollapsed) {
                    isCollapsed = false;
                    setToolbarColorExpanded(activity, collapsingToolbarLayout, toolbar);
                }
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                CharSequence title = navDestination.getLabel();
                if (title != null) {
                    collapsingToolbarLayout.setTitle(title);
                }
            }
        });
    }

    default void setToolbarColorExpanded(
            AppCompatActivity activity,
            CollapsingToolbarLayout collapsingToolbarLayout,
            Toolbar toolbar) {
        if (ThemeUtil.isDarkMode(activity)) {
            collapsingToolbarLayout.setExpandedTitleColor(
                    ContextCompat.getColor(
                            activity,
                            R.color.text_color));
            toolbar.getOverflowIcon().setTint(
                    ContextCompat.getColor(
                            activity,
                            R.color.text_color));
            tintNavigationIcon(activity, toolbar, R.color.text_color);
        } else {
            collapsingToolbarLayout.setExpandedTitleColor(
                    ContextCompat.getColor(
                            activity,
                            R.color.bg_color));
            toolbar.getOverflowIcon().setTint(
                    ContextCompat.getColor(
                            activity,
                            R.color.bg_color));
            tintNavigationIcon(activity, toolbar, R.color.bg_color);
        }
        StatusBarUtil.setupStatusBarBackgroundColor(activity, R.color.white);
        LayoutUtil.setupLayoutFullscreen(activity);
    }

    private void setToolbarColorCollapsed(
            AppCompatActivity activity,
            CollapsingToolbarLayout collapsingToolbarLayout,
            Toolbar toolbar
    ) {
        collapsingToolbarLayout.setCollapsedTitleTextColor(
                ContextCompat.getColor(
                        activity,
                        R.color.text_color));
        toolbar.getOverflowIcon().setTint(
                ContextCompat.getColor(
                        activity,
                        R.color.text_color));
        tintNavigationIcon(activity, toolbar, R.color.text_color);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ThemeUtil.isDarkMode(activity)) {
                StatusBarUtil.setupStatusBarBackgroundColor(activity, R.color.bg_color);
                LayoutUtil.setupLayoutFullscreen(activity);

            } else {
                StatusBarUtil.setupStatusBarBackgroundColor(activity, R.color.bg_color);
                LayoutUtil.setupLayoutFullscreen(activity);
            }
        }
    }

    default void tintNavigationIcon(AppCompatActivity appCompatActivity, Toolbar toolbar, int colorResId) {
        if (toolbar.getNavigationIcon() != null) {
            toolbar.getNavigationIcon().setColorFilter(
                    ContextCompat.getColor(appCompatActivity, colorResId),
                    PorterDuff.Mode.SRC_ATOP
            );
        }
    }
}
