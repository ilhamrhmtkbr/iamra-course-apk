package com.ilhamrhmtkbr.presentation.student.sections;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.presentation.utils.helper.TableLayoutUtil;
import com.ilhamrhmtkbr.databinding.FragmentStudentSectionsBinding;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentSectionsResponse;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.domain.model.student.ProgressDetail;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SectionsFragment extends Fragment {
    private FragmentStudentSectionsBinding binding;
    private String courseId;
    private TableLayout sectionsResults;
    private SectionsViewModel viewModel;
    private Map<String, String> progress;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            courseId = getArguments().getString("course_id");
        }

        viewModel = new ViewModelProvider(this).get(SectionsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentSectionsBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        sectionsResults = binding.sections;

        setupTableSections();
        observeViewModel();
    }

    private void setupTableSections() {
        String[] headersText = {
                getString(R.string.title),
                getString(R.string.status),
                getString(R.string.actions)
        };
        sectionsResults.addView(TableLayoutUtil.setup(requireContext(), headersText));
    }

    private void observeViewModel() {
        setupProgressData();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                viewModel.request(courseId);
                setupSections();
            }
        }, 1000);
    }

    private void setupProgressData() {
        viewModel.fetchProgressDetail(courseId);
        viewModel.getProgressDetail().observe(getViewLifecycleOwner(), new Observer<Resource<ProgressDetail>>() {
            @Override
            public void onChanged(Resource<ProgressDetail> progressDetailResource) {
                LoadingUtil.setup(progressDetailResource.isLoading(), binding.loading);
                if (progressDetailResource.isSuccess()) {
                    if (progressDetailResource.getData() != null) {
                        progress = progressDetailResource.getData().getSections();
                    }
                }
            }
        });
    }

    private boolean isCompleted(String sectionId) {
        return progress != null && progress.containsKey(sectionId);
    }
    private void setupSections() {
        viewModel.getSections().observe(getViewLifecycleOwner(), new Observer<Resource<List<StudentSectionsResponse>>>() {
            @Override
            public void onChanged(Resource<List<StudentSectionsResponse>> listResource) {
                LoadingUtil.setup(listResource.isLoading(), binding.loading);
                List<StudentSectionsResponse> studentSectionsResponse = listResource.getData();
                if (studentSectionsResponse == null) {
                    binding.noData.setVisibility(VISIBLE);
                    binding.data.setVisibility(GONE);
                } else {
                    binding.noData.setVisibility(GONE);
                    binding.data.setVisibility(VISIBLE);

                    StudentSectionsResponse course = studentSectionsResponse.get(0);

                    Glide.with(binding.courseImage.getContext())
                            .load(BuildConfig.INSTRUCTOR_COURSE_IMAGE_URL + course.image)
                            .centerCrop()
                            .into(binding.courseImage);

                    binding.courseTitle.setText(course.title);
                    binding.courseDesc.setText(course.description);
                    binding.courseStatus.setValue(TextUtil.capitalize(course.status));
                    binding.courseInstructor.setValue(course.instructor_id);
                    binding.coursePrice.setValue(TextUtil.formatRupiah(Integer.parseInt(course.price)));
                    binding.courseLevel.setValue(TextUtil.capitalize(course.level));
                    binding.courseNotes.setText(course.notes);
                    binding.courseRequirements.setText(course.requirements);

                    int childCount = sectionsResults.getChildCount();
                    if (childCount > 1) {
                        sectionsResults.removeViews(1, childCount - 1);
                    }

                    Context context = requireContext();

                    for (int i = 0; i < studentSectionsResponse.get(0).sections.size(); i++) {
                        StudentSectionsResponse.Sections item = studentSectionsResponse.get(0).sections.get(i);
                        TableRow tableRow = new TableRow(context);

                        if (i % 2 == 0) {
                            tableRow.setBackgroundColor(context.getColor(R.color.third_bg_color));
                        } else {
                            tableRow.setBackgroundColor(context.getColor(R.color.bg_color));
                        }

                        String[] rowData = {
                                item.title,
                                isCompleted(item.id) ? getString(R.string.completed) : getString(R.string.uncompleted),
                                getString(R.string.detail)
                        };

                        for (int j = 0; j < rowData.length; j++) {
                            TextView textView = new TextView(context);
                            textView.setText(rowData[j]);

                            if (j == 1) {
                                if (isCompleted(item.id)) {
                                    textView.setTextColor(context.getColor(R.color.green_color));
                                } else {
                                    textView.setTextColor(context.getColor(R.color.red_color));
                                }
                            }

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

                            if (j == 1 || j == 2) {
                                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            }

                            if (j == 2) {
                                textView.setTextColor(context.getColor(R.color.blue_color));
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("editor", studentSectionsResponse.get(0).editor);
                                        bundle.putString("course_id", courseId);
                                        bundle.putString("section_id", item.id);
                                        bundle.putString("section_title", item.title);

                                        if (progress != null && progress.containsKey(item.id)) {
                                            bundle.putString("progress_id", progress.get(item.id));
                                        }

                                        NavController navController = NavHostFragment.findNavController(SectionsFragment.this);
                                        navController.navigate(R.id.nav_lessons, bundle);
                                    }
                                });
                            }
                            tableRow.addView(textView);
                        }
                        sectionsResults.addView(tableRow);
                    }
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