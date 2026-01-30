package com.ilhamrhmtkbr.presentation.instructor.earnings;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Earning;
import com.ilhamrhmtkbr.presentation.utils.adapter.PaginationAdapter;
import com.ilhamrhmtkbr.databinding.FragmentInstructorEarningsBinding;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;
import com.ilhamrhmtkbr.presentation.utils.helper.TableLayoutUtil;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EarningsFragment extends Fragment {
    private FragmentInstructorEarningsBinding binding;
    private EarningsViewModel viewModel;
    private TableLayout earningsResult;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(EarningsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorEarningsBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        earningsResult = binding.earningsResult;

        setupTableTransactions();
        setupPagination();
        setupListeners();
        setupViewModel();
    }

    private void setupTableTransactions() {
        String[] headersText = {
                getString(R.string.order_id),
                getString(R.string.course),
                getString(R.string.student),
                getString(R.string.price),
                getString(R.string.status),
                getString(R.string.created_at),
        };
        earningsResult.addView(TableLayoutUtil.setup(requireContext(), headersText));
    }

    private void setupPagination() {
        PaginationAdapter paginationAdapter = new PaginationAdapter(new PaginationAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(String url) {
                Uri uri = Uri.parse(url);
                String page = uri.getQueryParameter("page");
                viewModel.refreshEarnings(
                        page,
                        viewModel.getSort().getValue()
                );
            }
        });

        binding.pagination.setLayoutManager(new LinearLayoutManager(requireContext(), HORIZONTAL, false));
        binding.pagination.setAdapter(paginationAdapter);
        binding.scrollIndicator.attachToRecyclerView(binding.pagination);
        viewModel.getPaginationData();
        viewModel.getPaginationData().observe(getViewLifecycleOwner(), new Observer<List<Page>>() {
            @Override
            public void onChanged(List<Page> pages) {
                paginationAdapter.updateLinks(pages);
            }
        });
    }

    private void setupListeners() {
        binding.swipeRefresh.setColorSchemeResources(R.color.blue_color);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshEarnings("1", "desc");
            }
        });

        binding.buttonWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.payout();
            }
        });

        binding.filterNew.setOnClickListener(v -> {
            viewModel.getSort().setValue("desc");
            viewModel.refreshEarnings("1", "desc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.blue_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.text_color));
        });

        binding.filterOld.setOnClickListener(v -> {
            viewModel.getSort().setValue("asc");
            viewModel.refreshEarnings("1", "asc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.text_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.blue_color));
        });
    }

    private void setupViewModel() {
        viewModel.getEarnings().observe(getViewLifecycleOwner(), new Observer<Resource<List<Earning>>>() {
            @Override
            public void onChanged(Resource<List<Earning>> listResource) {
                LoadingUtil.setupWithSwipeRefresh(listResource.isLoading(), binding.loading, binding.swipeRefresh);
                if (listResource.isSuccess()) {
                    if (listResource.getData() == null || listResource.getData().isEmpty()) {
                        binding.noData.setVisibility(VISIBLE);
                        binding.data.setVisibility(GONE);
                    } else {
                        binding.noData.setVisibility(GONE);
                        binding.data.setVisibility(VISIBLE);

                        int childCount = earningsResult.getChildCount();
                        if (childCount > 1) {
                            earningsResult.removeViews(1, childCount - 1);
                        }

                        Context context = requireContext();

                        for (int i = 0; i < listResource.getData().size(); i++) {
                            Earning item = listResource.getData().get(i);
                            TableRow tableRow = new TableRow(context);

                            if (i % 2 == 0) {
                                tableRow.setBackgroundColor(context.getColor(R.color.third_bg_color));
                            } else {
                                tableRow.setBackgroundColor(context.getColor(R.color.bg_color));
                            }

                            String[] rowData = {
                                    item.getOrder_id(),
                                    item.getInstructorCourse(),
                                    item.getStudentFullName(),
                                    item.getAmount(),
                                    item.getStatus(),
                                    item.getCreatedAt()
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

                                if (j != 1) {
                                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    textView.setGravity(Gravity.CENTER);
                                }

                                tableRow.addView(textView);
                            }

                            earningsResult.addView(tableRow);
                        }
                    }
                } else if (listResource.isError()) {
                    binding.noData.setVisibility(VISIBLE);
                    binding.data.setVisibility(GONE);
                }
            }
        });
        viewModel.getPayoutResult().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(stringFormState, binding.loading,
                        new Runnable() {
                            @Override
                            public void run() {
                                viewModel.refreshEarnings("1", "desc");
                                DialogUtil.showSuccessSnackbar(binding.getRoot(), stringFormState.getMessage());
                            }
                        },
                        new Runnable() {
                            @Override
                            public void run() {
                                viewModel.refreshEarnings("1", "desc");
                                DialogUtil.showErrorSnackbar(binding.getRoot(), stringFormState.getMessage());
                            }
                        });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
