package com.ilhamrhmtkbr.presentation.guest.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HomeInfoAdapter extends FragmentStateAdapter {
    public HomeInfoAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new HomeViewPagerStudent();
        } else {
            return new HomeViewPagerInstructor();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
