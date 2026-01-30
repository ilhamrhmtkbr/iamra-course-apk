package com.ilhamrhmtkbr.presentation.guest.courses;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;
import com.ilhamrhmtkbr.core.utils.notif.ModalFilter;
import com.ilhamrhmtkbr.presentation.utils.adapter.PaginationAdapter;
import com.ilhamrhmtkbr.databinding.FragmentPublicCoursesBinding;
import com.ilhamrhmtkbr.presentation.guest.coursedetail.CourseDetailFragment;
import com.ilhamrhmtkbr.presentation.utils.helper.RecyclerViewsUtil;

import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CoursesFragment extends Fragment {
    private FragmentPublicCoursesBinding binding;
    private CoursesViewModel viewModel;
    private CourseFilterParams filterParams;
    private CoursesAdapter courseAdapter;
    private PaginationAdapter paginationAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CoursesViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPublicCoursesBinding.inflate(inflater, container, false);
        setupBinding();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupFilters();

        // Check kalo ada search query dari arguments (dari SearchView)
        if (getArguments() != null) {
            String searchQuery = getArguments().getString("search_query");
            if (searchQuery != null && !searchQuery.isEmpty()) {
                performSearch(searchQuery);
            } else {
                fetchCourses();
            }
        } else {
            fetchCourses();
        }

        setupListeners();
        observeViewModel();
    }

    private void setupBinding() {
        courseAdapter = new CoursesAdapter();
        binding.rvCourses.addItemDecoration(new RecyclerViewsUtil.VerticalSpaceItemDecoration(50));
        binding.rvCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCourses.setAdapter(courseAdapter);

        paginationAdapter = new PaginationAdapter(new PaginationAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(String url) {
                Uri uri = Uri.parse(url);
                String page = uri.getQueryParameter("page");
                updateFilter(filterParams.keyword, page, filterParams.orderBy, filterParams.level, filterParams.status);
            }
        });
        binding.rvPagination.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvPagination.setAdapter(paginationAdapter);
        binding.scrollIndicator.attachToRecyclerView(binding.rvPagination);
    }

    private void setupFilters() {
        filterParams = new CourseFilterParams("", "1", "new", "all", "all");
        viewModel.setFilterParams(filterParams);
    }

    private void fetchCourses() {
        viewModel.fetch();
    }

    public void performSearch(String query) {
        updateFilter(query, "1", filterParams.orderBy, filterParams.level, filterParams.status);
    }

    private void observeViewModel() {
        viewModel.getCoursesResult().observe(getViewLifecycleOwner(), new Observer<Resource<List<Course>>>() {
            @Override
            public void onChanged(Resource<List<Course>> listResource) {
                if (listResource != null) {
                    LoadingUtil.setup(listResource.isLoading(), binding.loading);
                    if(listResource.isSuccess()) {
                        if (listResource.getData() != null) {
                            courseAdapter.setCourseList(listResource.getData());
                        }
                    }
                }
            }
        });

        viewModel.getCoursesPagination().observe(getViewLifecycleOwner(), new Observer<Resource<List<Page>>>() {
            @Override
            public void onChanged(Resource<List<Page>> listResource) {
                if (listResource != null && listResource.getData() != null) {
                    paginationAdapter.updateLinks(listResource.getData());
                }
            }
        });

        viewModel.getCurrentParams().observe(getViewLifecycleOwner(), new Observer<CourseFilterParams>() {
            @Override
            public void onChanged(CourseFilterParams courseFilterParams) {
                viewModel.fetch();
            }
        });
    }

    private void setupListeners() {
        courseAdapter.setOnCourseClickListener(courseId -> {
            CourseDetailFragment detailFragment = new CourseDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("course_id", courseId);
            detailFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_content, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Filter by sort
        binding.buttonFilterBySort.setOnClickListener(v -> showFilterDialog(
                R.string.filter_by_created_at,
                Arrays.asList(getString(R.string.latest), getString(R.string.longest)),
                option -> {
                    String sort = option.equals(getString(R.string.latest)) ? "new" : "old";
                    updateFilter(filterParams.keyword, "1", sort, filterParams.level, filterParams.status);
                }
        ));

        // Filter by level
        binding.buttonFilterByLevel.setOnClickListener(v -> showFilterDialog(
                R.string.filter_by_level,
                Arrays.asList("junior", "middle", "senior", "all"),
                level -> updateFilter(filterParams.keyword, "1", filterParams.orderBy, level, filterParams.status)
        ));

        // Filter by status
        binding.buttonFilterByStatus.setOnClickListener(v -> showFilterDialog(
                R.string.filter_by_status,
                Arrays.asList("paid", "free", "all"),
                status -> updateFilter(filterParams.keyword, "1", filterParams.orderBy, filterParams.level, status)
        ));
    }

    private void updateFilter(String keyword, String page, String orderBy, String level, String status) {
        filterParams = new CourseFilterParams(keyword, page, orderBy, level, status);
        viewModel.setFilterParams(filterParams);
    }

    private void showFilterDialog(int titleResId, List<String> options, ModalFilter.OnSortOptionSelectedListener listener) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.component_modal_filter);

        ((TextView) dialog.findViewById(R.id.modal_title)).setText(titleResId);
        dialog.findViewById(R.id.modal_close).setOnClickListener(v -> dialog.dismiss());

        RecyclerView recyclerView = dialog.findViewById(R.id.options);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(new ModalFilter.SortOptionAdapter(options, option -> {
            listener.onSortOptionSelected(option);
            dialog.dismiss();
        }));

        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}