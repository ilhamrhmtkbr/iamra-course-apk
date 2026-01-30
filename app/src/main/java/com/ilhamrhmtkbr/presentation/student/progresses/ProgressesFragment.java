package com.ilhamrhmtkbr.presentation.student.progresses;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import com.ilhamrhmtkbr.presentation.utils.helper.TableLayoutUtil;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Progress;
import com.ilhamrhmtkbr.presentation.utils.adapter.PaginationAdapter;
import com.ilhamrhmtkbr.databinding.FragmentStudentProgressesBinding;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProgressesFragment extends Fragment {
    private FragmentStudentProgressesBinding binding;
    private ProgressesViewModel viewModel;
    private TableLayout progressesResult;
    private PaginationAdapter paginationAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(ProgressesViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentProgressesBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        progressesResult = binding.progresses;

        setupAdapters();
        setupSwipeRefresh();
        setupPagination();
        setupTableProgresses();
        setupViewModel();
    }

    private void setupAdapters() {
        paginationAdapter = new PaginationAdapter(new PaginationAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(String url) {
                Uri uri = Uri.parse(url);
                String page = uri.getQueryParameter("page");
                viewModel.refreshProgresses(page);
            }
        });
    }

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeResources(R.color.blue_color);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshProgresses("1");
            }
        });
    }

    private void setupPagination() {
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

    private void setupTableProgresses() {
        String[] headersText = {
                getString(R.string.title),
                getString(R.string.completed_sections),
                getString(R.string.total_sections),
                getString(R.string.actions)
        };

        progressesResult.addView(TableLayoutUtil.setup(requireContext(), headersText));
    }

    private void setupViewModel() {
        viewModel.getProgresses().observe(getViewLifecycleOwner(), new Observer<Resource<List<Progress>>>() {
            @Override
            public void onChanged(Resource<List<Progress>> listResource) {
                LoadingUtil.setupWithSwipeRefresh(listResource.isLoading(), binding.loading, binding.swipeRefresh);
                if (listResource.isSuccess()) {
                    if (listResource.getData() == null || listResource.getData().isEmpty()) {
                        binding.noData.setVisibility(VISIBLE);
                        binding.data.setVisibility(GONE);
                    } else {
                        binding.noData.setVisibility(GONE);
                        binding.data.setVisibility(VISIBLE);

                        int childCount = progressesResult.getChildCount();
                        if (childCount > 1) {
                            progressesResult.removeViews(1, childCount - 1);
                        }

                        Context context = requireContext();

                        for (int i = 0; i < listResource.getData().size(); i++) {
                            Progress item = listResource.getData().get(i);
                            TableRow tableRow = new TableRow(context);

                            if (i % 2 == 0) {
                                tableRow.setBackgroundColor(context.getColor(R.color.third_bg_color));
                            } else {
                                tableRow.setBackgroundColor(context.getColor(R.color.bg_color));
                            }

                            String[] rowData = {
                                    item.getTitle(),
                                    item.getCompletedSections(),
                                    item.getTotalSections(),
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

                                if (j != 0) {
                                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                }

                                if (j == 3) {
                                    textView.setTextColor(context.getColor(R.color.blue_color));
                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("course_id", item.getId());
                                            bundle.putString("total_sections", item.getTotalSections());
                                            bundle.putString("completed_sections", item.getCompletedSections());

                                            NavController navController = NavHostFragment.findNavController(ProgressesFragment.this);
                                            NavOptions navOptions = new NavOptions.Builder()
                                                    .setPopUpTo(R.id.nav_progresses, false)
                                                    .build();
                                            navController.navigate(R.id.nav_progress_detail, bundle, navOptions);
                                        }
                                    });
                                }
                                tableRow.addView(textView);
                            }
                            progressesResult.addView(tableRow);
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