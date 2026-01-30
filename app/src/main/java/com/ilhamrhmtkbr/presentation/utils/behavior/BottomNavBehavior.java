package com.ilhamrhmtkbr.presentation.utils.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

public class BottomNavBehavior extends CoordinatorLayout.Behavior<View> {

    public BottomNavBehavior() {
        super();
    }

    public BottomNavBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull View child,
            @NonNull View directTargetChild,
            @NonNull View target,
            int axes,
            int type) {
        // Hanya respond untuk vertical scroll
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(
            @NonNull CoordinatorLayout coordinatorLayout,
            @NonNull View child,
            @NonNull View target,
            int dx,
            int dy,
            @NonNull int[] consumed,
            int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        // dy > 0 = scroll down (hide bottom nav)
        // dy < 0 = scroll up (show bottom nav)

        float translationY = child.getTranslationY() + dy;

        // Batas atas: 0 (fully visible)
        // Batas bawah: child.getHeight() (fully hidden ke bawah)
        if (translationY > child.getHeight()) {
            translationY = child.getHeight();
        } else if (translationY < 0) {
            translationY = 0;
        }

        child.setTranslationY(translationY);
    }
}