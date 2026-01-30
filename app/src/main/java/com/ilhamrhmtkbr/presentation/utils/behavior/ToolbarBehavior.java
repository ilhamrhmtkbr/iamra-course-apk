package com.ilhamrhmtkbr.presentation.utils.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

public class ToolbarBehavior extends CoordinatorLayout.Behavior<View> {

    public ToolbarBehavior() {
        super();
    }

    public ToolbarBehavior(Context context, AttributeSet attrs) {
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

        // dy > 0 = scroll down (hide toolbar)
        // dy < 0 = scroll up (show toolbar)

        float translationY = child.getTranslationY() - dy;

        // Batas atas: 0 (fully visible)
        // Batas bawah: -child.getHeight() (fully hidden)
        if (translationY < -child.getHeight()) {
            translationY = -child.getHeight();
        } else if (translationY > 0) {
            translationY = 0;
        }

        child.setTranslationY(translationY);
    }
}