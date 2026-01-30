package com.ilhamrhmtkbr.presentation.student.transactions;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ilhamrhmtkbr.domain.model.student.Transaction;
import com.ilhamrhmtkbr.presentation.utils.adapter.PaginationAdapter;
import com.ilhamrhmtkbr.databinding.FragmentStudentTransactionsBinding;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TransactionsFragment extends Fragment {
    private FragmentStudentTransactionsBinding binding;
    private TransactionsViewModel viewModel;
    private TableLayout transactionResult;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentTransactionsBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        transactionResult = binding.transactions;

        setupTableTransactions();
        setupSwipeRefresh();
        setupPagination();
        setupListeners();
        setupViewModel();
    }

    private void setupTableTransactions() {
        String[] headersText = {
                getString(R.string.order_id), getString(R.string.price),
                getString(R.string.status), getString(R.string.created_at),
                getString(R.string.actions)
        };
        transactionResult.addView(TableLayoutUtil.setup(requireContext(), headersText));
    }

    private void setupPagination() {
        PaginationAdapter paginationAdapter = new PaginationAdapter(new PaginationAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(String url) {
                Uri uri = Uri.parse(url);
                String page = uri.getQueryParameter("page");
                viewModel.refreshTransactions(page, viewModel.getSort().getValue(), viewModel.getStatus().getValue());
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

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeResources(R.color.blue_color);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshTransactions("1", "desc", "all");
            }
        });
    }

    private void setupListeners() {
        binding.filterNew.setOnClickListener(v -> {
            viewModel.getSort().setValue("desc");
            viewModel.refreshTransactions("1", "desc", "all");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.blue_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.text_color));
        });

        binding.filterOld.setOnClickListener(v -> {
            viewModel.getSort().setValue("asc");
            viewModel.refreshTransactions("1", "asc", "all");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.text_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.blue_color));
        });
    }

    private void setupViewModel() {
        viewModel.getTransactions().observe(getViewLifecycleOwner(), new Observer<Resource<List<Transaction>>>() {
            @Override
            public void onChanged(Resource<List<Transaction>> listResource) {
                LoadingUtil.setupWithSwipeRefresh(listResource.isLoading(), binding.loading, binding.swipeRefresh);
                if (listResource.isSuccess()) {
                    if (listResource.getData() == null || listResource.getData().isEmpty()) {
                        binding.noData.setVisibility(VISIBLE);
                        binding.data.setVisibility(GONE);
                    } else {
                        binding.noData.setVisibility(GONE);
                        binding.data.setVisibility(VISIBLE);

                        int childCount = transactionResult.getChildCount();
                        if (childCount > 1) {
                            transactionResult.removeViews(1, childCount - 1);
                        }

                        Context context = requireContext();

                        for (int i = 0; i < listResource.getData().size(); i++) {
                            Transaction item = listResource.getData().get(i);
                            TableRow tableRow = new TableRow(context);

                            if (i % 2 == 0) {
                                tableRow.setBackgroundColor(context.getColor(R.color.third_bg_color));
                            } else {
                                tableRow.setBackgroundColor(context.getColor(R.color.bg_color));
                            }

                            String[] rowData = {
                                    item.getOrder_id(),
                                    item.getAmount(),
                                    item.getStatus(),
                                    item.getCreated_at(),
                                    getString(R.string.pay)
                            };

                            for (int j = 0; j < rowData.length; j++) {
                                TextView textView = new TextView(context);
                                if (j == 2) {
                                    textView.setText(TextUtil.capitalize(rowData[j]));
                                    if (rowData[2].equals("settlement")) {
                                        textView.setTextColor(context.getColor(R.color.green_color));
                                    } else {
                                        textView.setTextColor(context.getColor(R.color.red_color));
                                    }
                                } else {
                                    textView.setText(rowData[j]);
                                }
                                textView.setPadding(
                                        getResources().getDimensionPixelSize(R.dimen.size_m),
                                        getResources().getDimensionPixelSize(R.dimen.size_l),
                                        getResources().getDimensionPixelSize(R.dimen.size_m),
                                        getResources().getDimensionPixelSize(R.dimen.size_l)
                                );

                                if (j == 2 || j == 3 || j == 4) {
                                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    textView.setGravity(Gravity.CENTER);
                                }

                                if (j == 4) {
                                    if (rowData[2].equals("settlement")) {
                                        textView.setTextColor(context.getColor(R.color.link_color));
                                        textView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Toast.makeText(context, context.getString(R.string.you_ve_paid), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        textView.setTextColor(context.getColor(R.color.blue_color));
                                        try {
                                            JSONObject jsonObject = new JSONObject(item.getMidtransData());
                                            String token = jsonObject.optString("token", "");

                                            textView.setTextColor(context.getColor(R.color.blue_color));
                                            textView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("order_id", item.getOrder_id());
                                                    bundle.putString("token", token);

                                                    NavController navController = NavHostFragment.findNavController(TransactionsFragment.this);
                                                    NavOptions navOptions = new NavOptions.Builder()
                                                            .setPopUpTo(R.id.nav_transactions, false)
                                                            .build();
                                                    navController.navigate(R.id.nav_transactions_detail, bundle, navOptions);
                                                }
                                            });
                                        } catch (JSONException e) {
                                            Log.e("JSON Error", "Failed to parse midTransData", e);
                                        }
                                    }
                                }

                                tableRow.addView(textView);
                            }

                            transactionResult.addView(tableRow);
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
