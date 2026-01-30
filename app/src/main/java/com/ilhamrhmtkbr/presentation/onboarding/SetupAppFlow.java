package com.ilhamrhmtkbr.presentation.onboarding;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.guest.GuestActivity;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;

import java.util.ArrayList;
import java.util.List;

public interface SetupAppFlow {
    default void setupViewPager(
            Context context,
            ViewPager2 viewPager2,
            MaterialButton btnNext,
            MaterialButton btnPrev,
            MaterialButton btnGetStart,
            LinearProgressIndicator progressIndicator,
            TextView pageCounter) {
        List<OnboardingItem> items = new ArrayList<>();
        items.add(new OnboardingItem(R.drawable.onboarding1, context.getString(R.string.onboarding_title_1), context.getString(R.string.onboarding_desc_1)));
        items.add(new OnboardingItem(R.drawable.onboarding2, context.getString(R.string.student), context.getString(R.string.onboarding_desc_2)));
        items.add(new OnboardingItem(R.drawable.onboarding3, context.getString(R.string.instructor), context.getString(R.string.onboarding_desc_3)));

        OnboardingAdapter adapter = new OnboardingAdapter(items);
        viewPager2.setAdapter(adapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentPosition(position);
                updateUI(btnNext, btnPrev, btnGetStart, progressIndicator, pageCounter);
            }
        });
        updateUI(btnNext, btnPrev, btnGetStart, progressIndicator, pageCounter);
    }

    default void setupClickListeners(MaterialButton btnNext, MaterialButton btnPrev, MaterialButton btnGetStart, ViewPager2 viewPager2) {
        btnNext.setOnClickListener(v -> {
            if (getCurrentPosition() < 3 - 1) {
                viewPager2.setCurrentItem(getCurrentPosition() + 1, true);
            }
        });

        btnPrev.setOnClickListener(v -> {
            if (getCurrentPosition() > 0) {
                viewPager2.setCurrentItem(getCurrentPosition() - 1, true);
            }
        });

        btnGetStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectUtil.redirectToActivity(btnNext.getContext(), GuestActivity.class);
            }
        });
    }

    default void updateUI(
            MaterialButton btnNext, MaterialButton btnPrev, MaterialButton btnGetStart,
            LinearProgressIndicator progressIndicator, TextView pageCounter
    ) {
        int totalPages = 3;
        boolean isLastPage = getCurrentPosition() == totalPages - 1;
        boolean isFirstPage = getCurrentPosition() == 0;

        btnNext.setVisibility(isLastPage ? View.GONE : View.VISIBLE);
        btnGetStart.setVisibility(isLastPage ? View.VISIBLE : View.GONE);
        btnPrev.setVisibility(isFirstPage ? View.INVISIBLE : View.VISIBLE);

        progressIndicator.setProgress((getCurrentPosition() + 1) * 100 / totalPages);
        pageCounter.setText(String.format("%d dari %d", getCurrentPosition() + 1, totalPages));
    }

    int getCurrentPosition();

    void setCurrentPosition(int number);
}
