package com.ilhamrhmtkbr.presentation.instructor.socials;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.databinding.FragmentInstructorSocialsBinding;
import com.ilhamrhmtkbr.domain.model.instructor.Social;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SocialsFragment extends Fragment {
    private FragmentInstructorSocialsBinding binding;
    private SocialsViewModel viewModel;
    private TableLayout socialsResult;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(SocialsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorSocialsBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        socialsResult = binding.socialsResult;

        setupTableTransactions();
        setupElements();
        setupViewModel();
    }

    private void setupTableTransactions() {
        TableRow headerRow = new TableRow(requireContext());
        String[] headerText = {
                getString(R.string.link),
                getString(R.string.app),
                getString(R.string.display_name),
                getString(R.string.actions)
        };

        for (int i = 0; i < headerText.length; i++) {
            TextView headerView = new TextView(requireContext());
            headerView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            headerView.setText(headerText[i]);
            headerView.setPadding(
                    getResources().getDimensionPixelSize(R.dimen.size_m),
                    getResources().getDimensionPixelSize(R.dimen.size_s),
                    getResources().getDimensionPixelSize(R.dimen.size_m),
                    getResources().getDimensionPixelSize(R.dimen.size_s)
            );
            headerView.setTextColor(getResources().getColor(R.color.second_bg_color));

            if (i == 0) {
                headerView.setBackgroundResource(R.drawable.table_cell_item_header_left);
            } else if (i == headerText.length - 1) {
                headerView.setBackgroundResource(R.drawable.table_cell_item_header_right);
            } else {
                headerView.setBackgroundColor(getResources().getColor(R.color.text_color));
            }
            headerView.setTypeface(null, Typeface.BOLD); // text tebal
            headerRow.addView(headerView);
        }

        socialsResult.addView(headerRow);
    }

    private void setupElements() {
        binding.buttonAddSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("form_type", "add");

                NavController navController = NavHostFragment.findNavController(SocialsFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_socials, false)
                        .build();
                navController.navigate(R.id.nav_social, bundle, navOptions);
            }
        });

        binding.swipeRefresh.setColorSchemeResources(R.color.blue_color);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshSocials();
            }
        });

        viewModel.refreshSocials();
    }

    private void setupViewModel() {
        viewModel.getSocials().observe(getViewLifecycleOwner(), new Observer<Resource<List<Social>>>() {
            @Override
            public void onChanged(Resource<List<Social>> listResource) {
                if (listResource.getData() == null || listResource.getData().isEmpty()) {
                    binding.noData.setVisibility(VISIBLE);
                    binding.data.setVisibility(GONE);
                } else {
                    binding.noData.setVisibility(GONE);
                    binding.data.setVisibility(VISIBLE);

                    int childCount = socialsResult.getChildCount();
                    if (childCount > 1) {
                        socialsResult.removeViews(1, childCount - 1);
                    }

                    Context context = requireContext();

                    for (int i = 0; i < listResource.getData().size(); i++) {
                        Social item = listResource.getData().get(i);
                        TableRow tableRow = new TableRow(context);

                        if (i % 2 == 0) {
                            tableRow.setBackgroundColor(context.getColor(R.color.third_bg_color));
                        } else {
                            tableRow.setBackgroundColor(context.getColor(R.color.bg_color));
                        }

                        String[] rowData = {
                                item.getUrLink(),
                                item.getAppName(),
                                item.getDisplayName(),
                                getString(R.string.edit)
                        };

                        for (int j = 0; j < rowData.length; j++) {
                            TextView textView = new TextView(context);
                            textView.setPadding(
                                    getResources().getDimensionPixelSize(R.dimen.size_m),
                                    getResources().getDimensionPixelSize(R.dimen.size_l),
                                    getResources().getDimensionPixelSize(R.dimen.size_m),
                                    getResources().getDimensionPixelSize(R.dimen.size_l)
                            );

                            if (j == 3){
                                textView.setTextColor(requireContext().getColor(R.color.blue_color));

                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("form_type", "edit");
                                        bundle.putString("id", item.getId());
                                        bundle.putString("url_link", item.getUrLink());
                                        bundle.putString("display_name", item.getDisplayName());

                                        NavController navController = NavHostFragment.findNavController(SocialsFragment.this);
                                        NavOptions navOptions = new NavOptions.Builder()
                                                .setPopUpTo(R.id.nav_socials, false)
                                                .build();
                                        navController.navigate(R.id.nav_social, bundle, navOptions);
                                    }
                                });
                            }
                            if (j != 0) {
                                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                textView.setGravity(Gravity.CENTER);
                            }

                            textView.setText(rowData[j]);
                            tableRow.addView(textView);
                        }

                        socialsResult.addView(tableRow);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        binding = null;
    }
}
