package com.ilhamrhmtkbr.presentation.instructor.courses;

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
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.presentation.utils.adapter.PaginationAdapter;
import com.ilhamrhmtkbr.databinding.FragmentInstructorCoursesBinding;
import com.ilhamrhmtkbr.presentation.utils.helper.RecyclerViewsUtil;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CoursesFragment extends Fragment {
    private FragmentInstructorCoursesBinding binding;
    private CoursesViewModel viewModel;
    private CoursesAdapter coursesAdapter;
    private PaginationAdapter paginationAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(CoursesViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorCoursesBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupCourses();
        setupPagination();
        setupListeners();
        observeViewModels();
    }

    private void setupCourses() {
        coursesAdapter = new CoursesAdapter(new CoursesAdapter.onClickListener() {
            @Override
            public void addCoupon(String courseId) {
                Bundle bundle = new Bundle();
                bundle.putString("form_type", "add");
                bundle.putString("course_id", courseId);

                NavController navController = NavHostFragment.findNavController(CoursesFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_courses, false)
                        .build();
                navController.navigate(R.id.nav_coupon, bundle, navOptions);
            }

            @Override
            public void section(String courseId, String courseTitle, String courseEditor) {
                Bundle bundle = new Bundle();
                bundle.putString("course_id", courseId);
                bundle.putString("course_title", courseTitle);
                bundle.putString("course_editor", courseEditor);

                NavController navController = NavHostFragment.findNavController(CoursesFragment.this);
                navController.navigate(R.id.nav_sections, bundle);
            }

            @Override
            public void edit(String courseId) {
                Bundle bundle = new Bundle();
                bundle.putString("form_type", "edit");
                bundle.putString("course_id", courseId);

                NavController navController = NavHostFragment.findNavController(CoursesFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_courses, false)
                        .build();
                navController.navigate(R.id.nav_course, bundle, navOptions);
            }
        });

        binding.rv.addItemDecoration(new RecyclerViewsUtil.VerticalSpaceItemDecoration(50));
        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rv.setAdapter(coursesAdapter);
    }

    private void setupPagination() {
        paginationAdapter = new PaginationAdapter(new PaginationAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(String url) {
                Uri uri = Uri.parse(url);
                String page = uri.getQueryParameter("page");
                viewModel.refreshCourses(page, viewModel.getSort().getValue());
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

    private void setupListeners() {
        binding.swipeRefresh.setColorSchemeResources(R.color.blue_color);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshCourses("1", "desc");
            }
        });

        binding.buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("form_type", "add");

                NavController navController = NavHostFragment.findNavController(CoursesFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_courses, false)
                        .build();
                navController.navigate(R.id.nav_course, bundle, navOptions);
            }
        });

        binding.filterNew.setOnClickListener(v -> {
            viewModel.getSort().setValue("desc");
            viewModel.refreshCourses("1", "desc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.blue_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.text_color));
        });

        binding.filterOld.setOnClickListener(v -> {
            viewModel.getSort().setValue("asc");
            viewModel.refreshCourses("1", "asc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.text_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.blue_color));
        });
    }

    private void observeViewModels() {
        viewModel.getCourses().observe(getViewLifecycleOwner(), new Observer<Resource<List<Course>>>() {
            @Override
            public void onChanged(Resource<List<Course>> listResource) {
                LoadingUtil.setupWithSwipeRefresh(listResource.isLoading(), binding.loading, binding.swipeRefresh);
                if (listResource.isSuccess()) {
                    if (listResource.getData() != null) {
                        binding.noData.setVisibility(GONE);
                        binding.data.setVisibility(VISIBLE);
                        coursesAdapter.setData(listResource.getData());
                    } else {
                        binding.noData.setVisibility(VISIBLE);
                        binding.data.setVisibility(GONE);
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
