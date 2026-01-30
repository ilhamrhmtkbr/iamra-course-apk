package com.ilhamrhmtkbr.presentation.instructor.coupons;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

import android.content.Context;
import android.net.Uri;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Coupon;
import com.ilhamrhmtkbr.presentation.utils.adapter.PaginationAdapter;
import com.ilhamrhmtkbr.databinding.FragmentInstructorCouponsBinding;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;
import com.ilhamrhmtkbr.presentation.utils.helper.TableLayoutUtil;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CouponsFragment extends Fragment {
    private FragmentInstructorCouponsBinding binding;
    private CouponsViewModel viewModel;
    private TableLayout couponsResult;
    private PaginationAdapter paginationAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(CouponsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorCouponsBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        couponsResult = binding.couponsResult;

        setupPagination();
        setupTableLessons();
        setupListeners();
        setupViewModels();
    }

    private void setupPagination() {
        paginationAdapter = new PaginationAdapter(new PaginationAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(String url) {
                Uri uri = Uri.parse(url);
                String page = uri.getQueryParameter("page");
                viewModel.refreshCoupons(page, viewModel.getSort().getValue());
            }
        });

        binding.pagination.setLayoutManager(new LinearLayoutManager(requireContext(), HORIZONTAL, false));
        binding.pagination.setAdapter(paginationAdapter);
        binding.scrollIndicator.attachToRecyclerView(binding.pagination);
        viewModel.getPaginationData().observe(getViewLifecycleOwner(), new Observer<List<Page>>() {
            @Override
            public void onChanged(List<Page> pages) {
                paginationAdapter.updateLinks(pages);

            }
        });
    }

    private void setupTableLessons() {
        String[] headersText = {
                "Id",
                getString(R.string.actions)
        };
        couponsResult.addView(TableLayoutUtil.setup(requireContext(), headersText));
    }

    private void setupListeners() {
        binding.swipeRefresh.setColorSchemeResources(R.color.blue_color);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshCoupons("1", "desc");
            }
        });

        binding.filterNew.setOnClickListener(v -> {
            viewModel.getSort().setValue("desc");
            viewModel.refreshCoupons("1", "desc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.blue_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.text_color));

        });

        binding.filterOld.setOnClickListener(v -> {
            viewModel.getSort().setValue("asc");
            viewModel.refreshCoupons("1", "asc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.text_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.blue_color));

        });
    }

    private void setupViewModels() {
        viewModel.getCoupons().observe(getViewLifecycleOwner(), new Observer<Resource<List<Coupon>>>() {
            @Override
            public void onChanged(Resource<List<Coupon>> listResource) {
                LoadingUtil.setupWithSwipeRefresh(listResource.isLoading(), binding.loading, binding.swipeRefresh);
                if (listResource.isSuccess()) {
                    if (listResource.getData() == null || listResource.getData().isEmpty()) {
                        binding.noData.setVisibility(VISIBLE);
                        binding.data.setVisibility(GONE);
                    } else {
                        binding.noData.setVisibility(GONE);
                        binding.data.setVisibility(VISIBLE);

                        int childCount = couponsResult.getChildCount();
                        if (childCount > 1) {
                            couponsResult.removeViews(1, childCount - 1);
                        }

                        Context context = requireContext();

                        for (int i = 0; i < listResource.getData().size(); i++) {
                            Coupon item = listResource.getData().get(i);
                            TableRow tableRow = new TableRow(context);

                            if (i % 2 == 0) {
                                tableRow.setBackgroundColor(context.getColor(R.color.third_bg_color));
                            } else {
                                tableRow.setBackgroundColor(context.getColor(R.color.bg_color));
                            }

                            String[] rowData = {
                                    item.getId(),
                                    getString(R.string.detail)
                            };

                            for (int j = 0; j < rowData.length; j++) {
                                TextView textView = new TextView(context);
                                textView.setText(rowData[j]);
                                textView.setPadding(
                                        getResources().getDimensionPixelSize(R.dimen.size_m),
                                        getResources().getDimensionPixelSize(R.dimen.size_l),
                                        getResources().getDimensionPixelSize(R.dimen.size_m),
                                        getResources().getDimensionPixelSize(R.dimen.size_l)
                                );

                                if (j == 0) {
                                    TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                                    textView.setLayoutParams(params);
                                }

                                if (j == 1) {
                                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    textView.setGravity(Gravity.CENTER);
                                }

                                if (j == 1) {
                                    textView.setTextColor(context.getColor(R.color.blue_color));
                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("coupon_id", item.getId());
                                            bundle.putString("form_type", "edit");

                                            NavController navController = NavHostFragment.findNavController(CouponsFragment.this);
                                            NavOptions navOptions = new NavOptions.Builder()
                                                    .setPopUpTo(R.id.nav_coupons, false)
                                                    .build();
                                            navController.navigate(R.id.nav_coupon, bundle, navOptions);
                                        }
                                    });
                                }
                                tableRow.addView(textView);
                            }
                            couponsResult.addView(tableRow);
                        }
                    }
                } else if (listResource.isError()) {
                    binding.noData.setVisibility(VISIBLE);
                    binding.data.setVisibility(GONE);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
