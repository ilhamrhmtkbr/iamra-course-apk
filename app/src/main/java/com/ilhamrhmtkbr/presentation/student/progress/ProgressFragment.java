package com.ilhamrhmtkbr.presentation.student.progress;

import android.graphics.Color;
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

import com.bumptech.glide.Glide;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.databinding.FragmentStudentProgessBinding;
import com.ilhamrhmtkbr.domain.model.student.ProgressDetail;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProgressFragment extends Fragment {
    private FragmentStudentProgessBinding binding;
    private ProgressViewModel viewModel;
    private String courseId;
    private String totalSections;
    private String completedSections;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(ProgressViewModel.class);

        if (getArguments() != null) {
            courseId = getArguments().getString("course_id");
            totalSections = getArguments().getString("total_sections");
            completedSections = getArguments().getString("completed_sections");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentProgessBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupListener();
        setupViewModel();
    }

    private void setupListener() {
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.submitCompletedCourse(courseId);
            }
        });
    }

    private void setupViewModel() {
        viewModel.getSubmitCompletedCourseResult().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<String>();
                formStateHandler.handle(stringFormState, binding.loading, null, new Runnable() {
                    @Override
                    public void run() {
                        NavController navController = NavHostFragment.findNavController(ProgressFragment.this);
                        NavOptions navOptions = new NavOptions.Builder()
                                .setPopUpTo(R.id.nav_progress_detail, true)
                                .build();
                        navController.navigate(R.id.nav_certificates, null, navOptions);
                    }
                });
            }
        });
        viewModel.getProgressDetail().observe(getViewLifecycleOwner(), new Observer<Resource<ProgressDetail>>() {
            @Override
            public void onChanged(Resource<ProgressDetail> progressDetailResource) {
                LoadingUtil.setup(progressDetailResource.isLoading(), binding.loading);
                if (progressDetailResource.isSuccess() && progressDetailResource.getData() != null) {
                    Glide.with(binding.courseImage.getContext())
                            .load(BuildConfig.INSTRUCTOR_COURSE_IMAGE_URL + progressDetailResource.getData().getInstructor_course().getImage())
                            .centerCrop()
                            .into(binding.courseImage);

                    binding.courseTitle.setText(progressDetailResource.getData().getInstructor_course().getTitle());
                    binding.courseDesc.setText(progressDetailResource.getData().getInstructor_course().getDescription());
                    binding.courseLevel.setValue(progressDetailResource.getData().getInstructor_course().getLevel());
                    binding.coursePrice.setValue(progressDetailResource.getData().getInstructor_course().getPrice());
                    binding.courseStatus.setValue(progressDetailResource.getData().getInstructor_course().getStatus());

                    float total = Float.parseFloat(totalSections);
                    float completed = Float.parseFloat(completedSections);
                    float remaining = total - completed;

                    ArrayList<PieEntry> entries = new ArrayList<>();
                    entries.add(new PieEntry(completed, getString(R.string.completed_sections)));
                    entries.add(new PieEntry(remaining, getString(R.string.total_sections)));

                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setColors(new int[]{
                            requireContext().getColor(R.color.blue_color),
                            requireContext().getColor(R.color.orange_color)
                    });

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(14f);
                    data.setValueTextColor(Color.WHITE);

                    binding.charts.setData(data);
                    binding.charts.setUsePercentValues(true);
                    binding.charts.setDrawHoleEnabled(false);
                    binding.charts.setHoleRadius(0f);
                    binding.charts.setTransparentCircleRadius(45f);
                    binding.charts.setCenterTextSize(14f);
                    binding.charts.setEntryLabelColor(requireContext().getColor(R.color.text_color));
                    binding.charts.getDescription().setEnabled(false);

                    Legend legend = binding.charts.getLegend();
                    legend.setTextColor(requireContext().getColor(R.color.text_color)); // Ganti sesuai kebutuhan
                    legend.setOrientation(Legend.LegendOrientation.VERTICAL); // <-- ini penting
                    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//                legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    legend.setDrawInside(false);
                    legend.setTextSize(14f);

                    // Refresh chart
                    binding.charts.invalidate();
                }
            }
        });

        viewModel.request(courseId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
