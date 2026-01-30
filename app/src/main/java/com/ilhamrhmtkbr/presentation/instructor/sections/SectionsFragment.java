package com.ilhamrhmtkbr.presentation.instructor.sections;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.presentation.utils.helper.TableLayoutUtil;
import com.ilhamrhmtkbr.databinding.FragmentInstructorSectionsBinding;
import com.ilhamrhmtkbr.domain.model.common.Section;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SectionsFragment extends Fragment {
    private FragmentInstructorSectionsBinding binding;
    private SectionsViewModel viewModel;
    private TableLayout sectionsResult;
    private String courseId;
    private String courseTitle;
    private String courseEditor;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            courseId = getArguments().getString("course_id");
            courseTitle = getArguments().getString("course_title");
            courseEditor = getArguments().getString("course_editor");
        }
        viewModel = new ViewModelProvider(this).get(SectionsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorSectionsBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        sectionsResult = binding.sectionsResult;

        setupTableTransactions();
        setupElements();
        setupViewModel();
    }

    private void setupTableTransactions() {
        String[] headersText = {
                getString(R.string.title),
                getString(R.string.order_in_course),
                getString(R.string.actions)
        };

        sectionsResult.addView(TableLayoutUtil.setup(requireContext(), headersText));
    }

    private void setupElements() {
        binding.courseTitle.setText(courseTitle);

        binding.buttonAddSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("form_type", "add");
                bundle.putString("course_id", courseId);
                bundle.putString("course_title", courseTitle);
                bundle.putString("course_editor", courseEditor);

                NavController navController = NavHostFragment.findNavController(SectionsFragment.this);
                navController.navigate(R.id.nav_section, bundle);
            }
        });
    }

    private void setupViewModel() {
        viewModel.getSections(courseId).observe(getViewLifecycleOwner(), new Observer<Resource<List<Section>>>() {
            @Override
            public void onChanged(Resource<List<Section>> listResource) {
                LoadingUtil.setup(listResource.isLoading(), binding.loading);
                if (listResource.getData() == null || listResource.getData().isEmpty()) {
                    binding.noData.setVisibility(VISIBLE);
                    binding.data.setVisibility(GONE);
                } else {
                    binding.noData.setVisibility(GONE);
                    binding.data.setVisibility(VISIBLE);

                    int childCount = sectionsResult.getChildCount();
                    if (childCount > 1) {
                        sectionsResult.removeViews(1, childCount - 1);
                    }

                    Context context = requireContext();

                    for (int i = 0; i < listResource.getData().size(); i++) {
                        Section item = listResource.getData().get(i);
                        TableRow tableRow = new TableRow(context);

                        if (i % 2 == 0) {
                            tableRow.setBackgroundColor(context.getColor(R.color.third_bg_color));
                        } else {
                            tableRow.setBackgroundColor(context.getColor(R.color.bg_color));
                        }

                        String[] rowData = {
                                item.getTitle(),
                                item.getOrderInCourse(),
                                getString(R.string.edit)
                        };

                        for (int j = 0; j < rowData.length; j++) {

                            if (j == 2){
                                TextView edit = new TextView(context);
                                edit.setPadding(
                                        getResources().getDimensionPixelSize(R.dimen.size_m),
                                        getResources().getDimensionPixelSize(R.dimen.size_l),
                                        getResources().getDimensionPixelSize(R.dimen.size_m),
                                        getResources().getDimensionPixelSize(R.dimen.size_l)
                                );
                                edit.setText(getString(R.string.edit));
                                edit.setTextColor(requireContext().getColor(R.color.blue_color));

                                TextView seeLesson = new TextView(context);
                                seeLesson.setPadding(
                                        getResources().getDimensionPixelSize(R.dimen.size_m),
                                        getResources().getDimensionPixelSize(R.dimen.size_l),
                                        getResources().getDimensionPixelSize(R.dimen.size_m),
                                        getResources().getDimensionPixelSize(R.dimen.size_l)
                                );
                                seeLesson.setText(getString(R.string.see_lesson));
                                seeLesson.setTextColor(requireContext().getColor(R.color.green_color));

                                LinearLayout actions = new LinearLayout(requireContext());
                                actions.addView(edit);
                                actions.addView(seeLesson);
                                actions.setOrientation(LinearLayout.HORIZONTAL);

                                edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("form_type", "edit");
                                        bundle.putString("course_id", courseId);
                                        bundle.putString("course_title", courseTitle);
                                        bundle.putString("course_editor", courseEditor);
                                        bundle.putString("section_id", item.getId());
                                        bundle.putString("section_title", item.getTitle());
                                        bundle.putString("section_order", item.getOrderInCourse());

                                        NavController navController = NavHostFragment.findNavController(SectionsFragment.this);
                                        navController.navigate(R.id.nav_section, bundle);
                                    }
                                });

                                seeLesson.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("course_editor", courseEditor);
                                        bundle.putString("section_id", item.getId());
                                        bundle.putString("section_title", item.getTitle());

                                        NavController navController = NavHostFragment.findNavController(SectionsFragment.this);
                                        navController.navigate(R.id.nav_lessons, bundle);
                                    }
                                });
                                tableRow.addView(actions);
                            } else {
                                TextView textView = new TextView(context);
                                textView.setPadding(
                                        getResources().getDimensionPixelSize(R.dimen.size_m),
                                        getResources().getDimensionPixelSize(R.dimen.size_l),
                                        getResources().getDimensionPixelSize(R.dimen.size_m),
                                        getResources().getDimensionPixelSize(R.dimen.size_l)
                                );

                                if (j != 0) {
                                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    textView.setGravity(Gravity.CENTER);
                                }

                                textView.setText(rowData[j]);
                                tableRow.addView(textView);
                            }
                        }

                        sectionsResult.addView(tableRow);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (courseId != null) viewModel.refresh(courseId);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
