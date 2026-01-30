package com.ilhamrhmtkbr.presentation.student.carts;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Cart;
import com.ilhamrhmtkbr.presentation.utils.adapter.PaginationAdapter;
import com.ilhamrhmtkbr.databinding.FragmentStudentCartsBinding;
import com.ilhamrhmtkbr.presentation.utils.helper.RecyclerViewsUtil;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CartFragment extends Fragment {
    private FragmentStudentCartsBinding binding;
    private CartViewModel viewModel;
    private CartAdapter cartAdapter;
    private PaginationAdapter paginationAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CartViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStudentCartsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        setupCarts();
        setupPagination();
        setupSwipeRefresh();
        setupListeners();
        observeViewModel();
    }

    private void setupCarts() {
        cartAdapter = new CartAdapter(new CartAdapter.onClickListeners() {
            @Override
            public void buy(String courseId, String courseImage, String courseTitle, String courseDesc,
                            String coursePrice, String courseLevel, String courseStatus) {
                Bundle bundle = new Bundle();
                bundle.putString("course_id", courseId);
                bundle.putString("course_image", courseImage);
                bundle.putString("course_title", courseTitle);
                bundle.putString("course_desc", courseDesc);
                bundle.putString("course_price", coursePrice);
                bundle.putString("course_level", courseLevel);
                bundle.putString("course_status", courseStatus);

                NavController navController = NavHostFragment.findNavController(CartFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_cart, false)
                        .build();
                navController.navigate(R.id.nav_transactions_store, bundle, navOptions);

            }

            @Override
            public void delete(String cartId) {
                viewModel.deleteCart(cartId);
            }
        });

        binding.rv.addItemDecoration(new RecyclerViewsUtil.VerticalSpaceItemDecoration(50));
        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rv.setAdapter(cartAdapter);
    }

    private void setupPagination() {
        paginationAdapter = new PaginationAdapter(new PaginationAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(String url) {
                Uri uri = Uri.parse(url);
                String page = uri.getQueryParameter("page");
                viewModel.refreshCarts(page, viewModel.getSort().getValue());
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
        // Set warna loading indicator
        binding.swipeRefresh.setColorSchemeResources(R.color.blue_color);

        // Handle swipe to refresh
        binding.swipeRefresh.setOnRefreshListener(() -> {
            viewModel.refreshCarts("1", viewModel.getSort().getValue());
        });
    }

    private void setupListeners() {
        binding.filterNew.setOnClickListener(v -> {
            viewModel.getSort().setValue("desc");
            viewModel.refreshCarts("1", "desc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.blue_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.text_color));
        });

        binding.filterOld.setOnClickListener(v -> {
            viewModel.getSort().setValue("asc");
            viewModel.refreshCarts("1", "asc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.text_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.blue_color));
        });
    }

    private void observeViewModel() {
        viewModel.getCarts().observe(getViewLifecycleOwner(), new Observer<Resource<List<Cart>>>() {
            @Override
            public void onChanged(Resource<List<Cart>> listResource) {
                LoadingUtil.setupWithSwipeRefresh(listResource.isLoading(), binding.loading, binding.swipeRefresh);
                if (listResource.isSuccess()) {
                    if (listResource.getData() == null || listResource.getData().isEmpty()) {
                        binding.noData.setVisibility(VISIBLE);
                        binding.data.setVisibility(GONE);
                    } else {
                        binding.noData.setVisibility(GONE);
                        binding.data.setVisibility(VISIBLE);
                        cartAdapter.setData(listResource.getData());
                    }
                } else if (listResource.isError()){
                    binding.noData.setVisibility(VISIBLE);
                    binding.data.setVisibility(GONE);
                }
            }
        });

        viewModel.getDeleteResult().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                LoadingUtil.setupWithSwipeRefresh(stringFormState.isLoading(), binding.loading, binding.swipeRefresh);
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(stringFormState, binding.loading, null, afterSuccess());
            }
        });
    }

    private Runnable afterSuccess() {
        return new Runnable() {
            @Override
            public void run() {
                viewModel.refreshCarts("1", "desc");
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        cartAdapter = null;
        paginationAdapter = null;
    }
}