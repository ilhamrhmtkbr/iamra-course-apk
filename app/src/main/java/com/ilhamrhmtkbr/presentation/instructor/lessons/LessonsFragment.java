package com.ilhamrhmtkbr.presentation.instructor.lessons;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.presentation.utils.helper.TableLayoutUtil;
import com.ilhamrhmtkbr.domain.model.common.Lesson;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.presentation.utils.adapter.PaginationAdapter;
import com.ilhamrhmtkbr.databinding.FragmentInstructorLessonsBinding;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LessonsFragment extends Fragment {
    private FragmentInstructorLessonsBinding binding;
    private LessonsViewModel viewModel;
    private TableLayout lessonsResult;
    private String courseEditor;
    private String sectionId;
    private String section_title;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            courseEditor = getArguments().getString("course_editor");
            sectionId = getArguments().getString("section_id");
            section_title = getArguments().getString("section_title");
        }
        viewModel = new ViewModelProvider(this).get(LessonsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorLessonsBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        lessonsResult = binding.lessonsResult;

        setupTableTransactions();
        setupElements();
        setupPagination();
        setupViewModel();
    }

    private void setupTableTransactions() {
        String[] headersText = {
                getString(R.string.title),
                getString(R.string.order_in_section),
                getString(R.string.actions)
        };

        lessonsResult.addView(TableLayoutUtil.setup(requireContext(), headersText));
    }

    private void setupElements() {
        binding.sectionTitle.setText(section_title);
        binding.buttonAddLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("form_type", "add");
                bundle.putString("course_editor", courseEditor);
                bundle.putString("section_id", sectionId);

                NavController navController = NavHostFragment.findNavController(LessonsFragment.this);
                navController.navigate(R.id.nav_lesson, bundle);
            }
        });
    }

    private void setupPagination() {
        PaginationAdapter paginationAdapter = new PaginationAdapter(new PaginationAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(String url) {
                Uri uri = Uri.parse(url);
                String page = uri.getQueryParameter("page");
                viewModel.refresh(page, sectionId);
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

    private void setupViewModel() {
        viewModel.getLessons(sectionId).observe(getViewLifecycleOwner(), new Observer<Resource<List<Lesson>>>() {
            @Override
            public void onChanged(Resource<List<Lesson>> listResource) {
                if (listResource.getData() == null || listResource.getData().isEmpty()) {
                    binding.noData.setVisibility(VISIBLE);
                    binding.data.setVisibility(GONE);
                } else {
                    Log.d("InstructorLessons", "Showing " + listResource.getData().size() + " items");

                    binding.noData.setVisibility(GONE);
                    binding.data.setVisibility(VISIBLE);

                    int childCount = lessonsResult.getChildCount();
                    if (childCount > 1) {
                        lessonsResult.removeViews(1, childCount - 1);
                    }

                    Context context = requireContext();

                    for (int i = 0; i < listResource.getData().size(); i++) {
                        Lesson item = listResource.getData().get(i);
                        TableRow tableRow = new TableRow(context);

                        if (i % 2 == 0) {
                            tableRow.setBackgroundColor(context.getColor(R.color.third_bg_color));
                        } else {
                            tableRow.setBackgroundColor(context.getColor(R.color.bg_color));
                        }

                        String[] rowData = {
                                item.getTitle(),
                                item.getOrderInSection(),
                                getString(R.string.edit)
                        };

                        for (int j = 0; j < rowData.length; j++) {
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

                            if (j == 2) {
                                textView.setTextColor(requireContext().getColor(R.color.blue_color));
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("form_type", "edit");
                                        bundle.putString("course_editor", courseEditor);
                                        bundle.putString("section_id", sectionId);
                                        bundle.putString("lesson_id", item.getId());
                                        bundle.putString("lesson_title", item.getTitle());
                                        bundle.putString("lesson_description", item.getDescription());
                                        bundle.putString("lesson_code", item.getCode());
                                        bundle.putString("lesson_order", item.getOrderInSection());

                                        NavController navController = NavHostFragment.findNavController(LessonsFragment.this);
                                        navController.navigate(R.id.nav_lesson, bundle);
                                    }
                                });
                            }
                            textView.setText(rowData[j]);

                            tableRow.addView(textView);
                        }

                        lessonsResult.addView(tableRow);
                    }
                }
            }
        });

        viewModel.refresh("1", sectionId);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh("1", sectionId);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        binding = null;
    }
}
