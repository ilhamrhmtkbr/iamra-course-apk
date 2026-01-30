package com.ilhamrhmtkbr.presentation.utils.helper;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class LoadingUtil {
    public static void setup(boolean isShow, ConstraintLayout loadingView) {
        if (isShow) {
            DisplayMetrics metrics = loadingView.getResources().getDisplayMetrics();
            int screenHeight = metrics.heightPixels;

            ViewGroup.LayoutParams params = loadingView.getLayoutParams();
            params.height = screenHeight;
            loadingView.setLayoutParams(params);

            loadingView.setVisibility(View.VISIBLE);
        } else {
            loadingView.setVisibility(View.GONE);
        }
    }

    public static void setupWithSwipeRefresh(boolean isShow, ConstraintLayout loadingView, SwipeRefreshLayout swipeRefreshLayout) {
        if(!isShow && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (isShow) {
            DisplayMetrics metrics = loadingView.getResources().getDisplayMetrics();
            int screenHeight = metrics.heightPixels;

            ViewGroup.LayoutParams params = loadingView.getLayoutParams();
            params.height = screenHeight;
            loadingView.setLayoutParams(params);

            loadingView.setVisibility(View.VISIBLE);
        } else {
            loadingView.setVisibility(View.GONE);
        }
    }
}
