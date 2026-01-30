package com.ilhamrhmtkbr.presentation.guest.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.utils.tools.LangUtil;
import com.ilhamrhmtkbr.core.utils.ui.ThemeUtil;
import com.ilhamrhmtkbr.core.utils.tools.FragmentUtil;
import com.ilhamrhmtkbr.databinding.FragmentPublicHomeBinding;
import com.ilhamrhmtkbr.presentation.utils.helper.RecyclerViewsUtil;
import com.ilhamrhmtkbr.presentation.guest.certificates.CertificatesFragment;
import com.ilhamrhmtkbr.presentation.guest.courses.CoursesFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {
    private FragmentPublicHomeBinding binding;
    private final List<Integer> originalIcons1 = Arrays.asList(
            R.drawable.ic_pgr_ajax, R.drawable.ic_pgr_cpp, R.drawable.ic_pgr_css,
            R.drawable.ic_pgr_docker, R.drawable.ic_pgr_express, R.drawable.ic_pgr_firebase,
            R.drawable.ic_pgr_git, R.drawable.ic_pgr_github, R.drawable.ic_pgr_html,
            R.drawable.ic_pgr_javascript
    );
    private final List<Integer> originalIcons2 = Arrays.asList(
            R.drawable.ic_pgr_laravel, R.drawable.ic_pgr_mongodb, R.drawable.ic_pgr_mysql,
            R.drawable.ic_pgr_pg_sql, R.drawable.ic_pgr_php, R.drawable.ic_pgr_python,
            R.drawable.ic_pgr_react, R.drawable.ic_pgr_sql_server, R.drawable.ic_pgr_svg,
            R.drawable.ic_pgr_vue
    );

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable autoScrollRunnable1, autoScrollRunnable2;
    private final int scrollAmount = 10;
    private final int delay = 30;
    @Inject
    AuthStateManager authStateManager;
    @Inject
    LangUtil langUtil;
    @Inject
    ThemeUtil themeUtil;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPublicHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupSetting();
        setupIfUser();
        setupAutoScroll(binding.rvAdv1, originalIcons1, true);
        setupAutoScroll(binding.rvAdv2, originalIcons2, false);
        setupInfoTab();

        return root;
    }

    private void setupSetting() {
        List<HomeSettingAdapter.HomeSettingItem> settingItems = Arrays.asList(
                new HomeSettingAdapter.HomeSettingItem("Light Mode", R.drawable.ic_public_home_fragment_setting_light_mode),
                new HomeSettingAdapter.HomeSettingItem("Dark Mode", R.drawable.ic_public_home_fragment_setting_dark_mode),
                new HomeSettingAdapter.HomeSettingItem("Lang Id", R.drawable.ic_public_home_fragment_setting_lang_id),
                new HomeSettingAdapter.HomeSettingItem("Lang En", R.drawable.ic_public_home_fragment_setting_lang_en)
        );

        HomeSettingAdapter adapter = new HomeSettingAdapter(settingItems, new HomeSettingAdapter.OnclickListener() {
            @Override
            public void run(String title) {
                switch (title) {
                    case "Light Mode":
                        themeUtil.setTheme(ThemeUtil.THEME_LIGHT);
                        break;
                    case "Dark Mode":
                        themeUtil.setTheme(ThemeUtil.THEME_DARK);
                        break;
                    case "Lang Id":
                        langUtil.setLang(LangUtil.LANG_ID);
                        requireActivity().recreate();
                        break;
                    case "Lang En":
                        langUtil.setLang(LangUtil.LANG_EN);
                        requireActivity().recreate();
                        break;
                }
            }
        });
        binding.rvSetting.setAdapter(adapter);
        binding.rvSetting.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        binding.scrollIndicator.attachToRecyclerView(binding.rvSetting);
    }

    private void setupIfUser() {
        if (!authStateManager.isLoggedIn()) {
            binding.cardUser.setVisibility(View.GONE);
        } else {
            binding.cardUser.setVisibility(View.VISIBLE);
            binding.profileUsername.setText(authStateManager.getCurrentUsername());
            binding.profileEmail.setText(authStateManager.getCurrentUserEmail());
            binding.profileName.setText(authStateManager.getCurrentUserFullName());
        }
    }

    private void setupAutoScroll(View recyclerViewView, List<Integer> originalIcons, boolean isRightScroll) {
        List<Integer> repeatedIcons = new ArrayList<>();
        for (int i = 0; i < 30; i++) repeatedIcons.addAll(originalIcons);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        HomeImageAdapter adapter = new HomeImageAdapter(repeatedIcons);

        if (recyclerViewView.getId() == binding.rvAdv1.getId()) {
            binding.rvAdv1.setLayoutManager(layoutManager);
            binding.rvAdv1.setAdapter(adapter);
            binding.rvAdv1.scrollToPosition(repeatedIcons.size() / 2);

            autoScrollRunnable1 = () -> {
                binding.rvAdv1.smoothScrollBy(isRightScroll ? scrollAmount : -scrollAmount, 0);
                int pos = layoutManager.findFirstVisibleItemPosition();
                if (isRightScroll && layoutManager.findLastVisibleItemPosition() > adapter.getItemCount() - 10) {
                    binding.rvAdv1.scrollToPosition(adapter.getItemCount() / 2);
                } else if (!isRightScroll && pos < 10) {
                    binding.rvAdv1.scrollToPosition(adapter.getItemCount() / 2);
                }
                handler.postDelayed(autoScrollRunnable1, delay);
            };
            handler.postDelayed(autoScrollRunnable1, delay);

        } else {
            binding.rvAdv2.setLayoutManager(layoutManager);
            binding.rvAdv2.setAdapter(adapter);
            binding.rvAdv2.scrollToPosition(repeatedIcons.size() / 2);

            autoScrollRunnable2 = () -> {
                binding.rvAdv2.smoothScrollBy(isRightScroll ? scrollAmount : -scrollAmount, 0);
                int pos = layoutManager.findFirstVisibleItemPosition();
                if (isRightScroll && layoutManager.findLastVisibleItemPosition() > adapter.getItemCount() - 10) {
                    binding.rvAdv2.scrollToPosition(adapter.getItemCount() / 2);
                } else if (!isRightScroll && pos < 10) {
                    binding.rvAdv2.scrollToPosition(adapter.getItemCount() / 2);
                }
                handler.postDelayed(autoScrollRunnable2, delay);
            };
            handler.postDelayed(autoScrollRunnable2, delay);
        }
    }

    private void setupInfoTab() {
        HomeInfoAdapter homeInfoAdapter = new HomeInfoAdapter(this);
        binding.infoDetails.setAdapter(homeInfoAdapter);

        new TabLayoutMediator(binding.infoTab, binding.infoDetails,
                (tab, position) -> {
                    View customView = LayoutInflater.from(getContext())
                            .inflate(R.layout.item_public_home_tab_info, null);

                    TextView text = customView.findViewById(R.id.tabText);

                    if (position == 0) {
                        text.setText(getString(R.string.student));
                    } else {
                        text.setText(getString(R.string.instructor));
                    }

                    tab.setCustomView(customView);
                }).attach();

        binding.infoTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTabColor(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                updateTabColor(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        for (int i = 0; i < binding.infoTab.getTabCount(); i++) {
            TabLayout.Tab tab = binding.infoTab.getTabAt(i);
            if (tab != null) {
                updateTabColor(tab, i == 0);
            }
        }
    }

    private void updateTabColor(TabLayout.Tab tab, boolean isSelected) {
        View view = tab.getCustomView();
        if (view != null) {
            ImageView icon = view.findViewById(R.id.tabIcon);
            TextView text = view.findViewById(R.id.tabText);

            int color = isSelected ? R.color.blue_color : R.color.link_color;
            icon.setColorFilter(ContextCompat.getColor(requireContext(), color));
            text.setTextColor(ContextCompat.getColor(requireContext(), color));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (autoScrollRunnable1 != null) handler.removeCallbacks(autoScrollRunnable1);
        if (autoScrollRunnable2 != null) handler.removeCallbacks(autoScrollRunnable2);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (autoScrollRunnable1 != null) handler.postDelayed(autoScrollRunnable1, delay);
        if (autoScrollRunnable2 != null) handler.postDelayed(autoScrollRunnable2, delay);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (autoScrollRunnable1 != null) handler.removeCallbacks(autoScrollRunnable1);
        if (autoScrollRunnable2 != null) handler.removeCallbacks(autoScrollRunnable2);
        binding = null;
    }
}
