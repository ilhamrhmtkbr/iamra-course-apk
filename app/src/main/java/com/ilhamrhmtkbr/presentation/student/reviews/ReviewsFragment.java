package com.ilhamrhmtkbr.presentation.student.reviews;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.ilhamrhmtkbr.domain.model.student.Review;
import com.ilhamrhmtkbr.presentation.utils.adapter.PaginationAdapter;
import com.ilhamrhmtkbr.databinding.FragmentStudentReviewsBinding;
import com.ilhamrhmtkbr.presentation.utils.helper.RecyclerViewsUtil;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReviewsFragment extends Fragment {
    private FragmentStudentReviewsBinding binding;
    private ReviewsViewModel viewModel;
    private ReviewsAdapter reviewsAdapter;
    private PaginationAdapter paginationAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(ReviewsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentReviewsBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupAdapters();
        setupRecyclerViewReviews();
        setupListeners();
        setupSwipeRefresh();
        setupPagination();
        setupViewModel();
    }

    private void setupAdapters() {
        reviewsAdapter = new ReviewsAdapter(new ReviewsAdapter.onClickListener() {
            @Override
            public void edit(String reviewId, String courseId, String courseTitle, String courseDesc, String review, String rating) {
                Bundle bundle = new Bundle();
                bundle.putString("form_type", "edit");
                bundle.putString("review_id", reviewId);
                bundle.putString("course_id", courseId);
                bundle.putString("course_title", courseTitle);
                bundle.putString("course_desc", courseDesc);
                bundle.putString("review", review);
                bundle.putString("rating", rating);

                NavController navController = NavHostFragment.findNavController(ReviewsFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_reviews, false)
                        .build();
                navController.navigate(R.id.nav_review, bundle, navOptions);
            }
        });

        paginationAdapter = new PaginationAdapter(new PaginationAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(String url) {
                Uri uri = Uri.parse(url);
                String page = uri.getQueryParameter("page");
                viewModel.refreshReviews(page, viewModel.getSort().getValue());
            }
        });
    }

    private void setupRecyclerViewReviews() {
        binding.rv.addItemDecoration(new RecyclerViewsUtil.VerticalSpaceItemDecoration(33));
        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rv.setAdapter(reviewsAdapter);
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

    private void setupSwipeRefresh() {
        binding.swipeRefresh.setColorSchemeResources(R.color.blue_color);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshReviews("1", "desc");
            }
        });
    }

    private void setupListeners() {
        binding.filterNew.setOnClickListener(v -> {
            viewModel.getSort().setValue("desc");
            viewModel.refreshReviews("1", "desc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.blue_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.text_color));

        });

        binding.filterOld.setOnClickListener(v -> {
            viewModel.getSort().setValue("asc");
            viewModel.refreshReviews("1", "asc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.text_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.blue_color));

        });
    }

    private void setupViewModel() {
        viewModel.getReviews().observe(getViewLifecycleOwner(), new Observer<Resource<List<Review>>>() {
            @Override
            public void onChanged(Resource<List<Review>> listResource) {
                LoadingUtil.setupWithSwipeRefresh(listResource.isLoading(), binding.loading, binding.swipeRefresh);
                if (listResource.isSuccess()) {
                    if (listResource.getData() == null || listResource.getData().isEmpty()) {
                        binding.noData.setVisibility(VISIBLE);
                        binding.data.setVisibility(GONE);
                    } else {
                        binding.noData.setVisibility(GONE);
                        binding.data.setVisibility(VISIBLE);
                        reviewsAdapter.setData(listResource.getData());
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
