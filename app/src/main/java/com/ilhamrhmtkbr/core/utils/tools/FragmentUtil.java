package com.ilhamrhmtkbr.core.utils.tools;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentUtil {
    public static void navigateTo(AppCompatActivity activity, Fragment fragment, int layoutId) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(layoutId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void navigateFromFragment(Fragment from, Fragment to, int layoutId) {
        FragmentTransaction transaction = from.getParentFragmentManager().beginTransaction();

        transaction.replace(layoutId, to);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
