package com.ilhamrhmtkbr.presentation.student.lessons;

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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.presentation.utils.helper.TableLayoutUtil;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.presentation.utils.adapter.PaginationAdapter;
import com.ilhamrhmtkbr.databinding.FragmentStudentLessonsBinding;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentLessonsResponse;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LessonsFragment extends Fragment {
    private FragmentStudentLessonsBinding binding;
    private LessonsViewModel viewModel;
    private String courseId;
    private String sectionId;
    private String sectionTitle;
    private String editor;
    private String isCompleted;
    private PaginationAdapter paginationAdapter;
    private TableLayout lessonsResult;
    private String lastPages = "";

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(LessonsViewModel.class);

        if (getArguments() != null) {
            courseId = getArguments().getString("course_id");
            sectionId = getArguments().getString("section_id");
            sectionTitle = getArguments().getString("section_title");
            editor = getArguments().getString("editor");
            if (getArguments().getString("isCompleted") != null) {
                isCompleted = TextUtil.check(getArguments().getString("isCompleted"), "");
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentLessonsBinding.inflate(layoutInflater, viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        lessonsResult = binding.lessonsResult;

        setupPagination();
        setupTableLessons();
        if (isCompleted == null) {
            setupListeners();
        }
        setupViewModel();
    }

    private void setupPagination() {
        paginationAdapter = new PaginationAdapter(new PaginationAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(String url) {
                Uri uri = Uri.parse(url);
                String page = uri.getQueryParameter("page");
                viewModel.request(sectionId, page);
                assert page != null;
                if (lastPages.equals("1")) {
                    binding.buttonFinish.setVisibility(VISIBLE);
                    binding.textWarningLesson.setVisibility(GONE);
                } else {
                    binding.buttonFinish.setVisibility(page.equals(lastPages) ? VISIBLE : GONE);
                    binding.textWarningLesson.setVisibility(page.equals(lastPages) ? GONE : VISIBLE);
                }
            }
        });

        binding.pagination.setLayoutManager(new LinearLayoutManager(requireContext(), HORIZONTAL, false));
        binding.pagination.setAdapter(paginationAdapter);
        binding.scrollIndicator.attachToRecyclerView(binding.pagination);
    }

    private void setupTableLessons() {
        String[] headersText = {
                getString(R.string.title),
                getString(R.string.actions)
        };

        lessonsResult.addView(TableLayoutUtil.setup(requireContext(), headersText));
    }

    private void setupListeners() {
        binding.buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.finish(courseId, sectionId, sectionTitle);
            }
        });
    }

    private void setupViewModel() {
        viewModel.request(sectionId, "1");
        viewModel.getLessonsResult().observe(getViewLifecycleOwner(), new Observer<Resource<StudentLessonsResponse>>() {
            @Override
            public void onChanged(Resource<StudentLessonsResponse> studentLessonsResponseResource) {
                LoadingUtil.setup(studentLessonsResponseResource.isLoading(), binding.loading);
                if (studentLessonsResponseResource.isSuccess()) {
                    if (studentLessonsResponseResource.getData().data == null || studentLessonsResponseResource.getData().data.isEmpty()) {
                        binding.noData.setVisibility(VISIBLE);
                        binding.data.setVisibility(GONE);
                    } else {
                        List<Page> pageList = studentLessonsResponseResource.getData().links;
                        paginationAdapter.updateLinks(pageList);
                        if (pageList.size() > 3) {
                            lastPages = pageList.get(pageList.size() - 2).getLabel();
                        } else {
                            lastPages = "1";
                        }
                        binding.noData.setVisibility(GONE);
                        binding.data.setVisibility(VISIBLE);

                        int childCount = lessonsResult.getChildCount();
                        if (childCount > 1) {
                            lessonsResult.removeViews(1, childCount - 1);
                        }

                        Context context = requireContext();

                        for (int i = 0; i < studentLessonsResponseResource.getData().data.size(); i++) {
                            StudentLessonsResponse.LessonItem item = studentLessonsResponseResource.getData().data.get(i);
                            TableRow tableRow = new TableRow(context);

                            if (i % 2 == 0) {
                                tableRow.setBackgroundColor(context.getColor(R.color.third_bg_color));
                            } else {
                                tableRow.setBackgroundColor(context.getColor(R.color.bg_color));
                            }

                            String[] rowData = {
                                    item.title,
                                    getString(R.string.detail)
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

                                if (j == 0) {
                                    TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                                    textView.setLayoutParams(params);
                                }

                                if (j == 1) {
                                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    textView.setGravity(Gravity.CENTER);
                                }

                                if (j == 1) {
                                    textView.setTextColor(context.getColor(R.color.blue_color));
                                    textView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("lesson_id", item.id);
                                            bundle.putString("lesson_title", item.title);
                                            bundle.putString("lesson_desc", item.description);
                                            bundle.putString("lesson_code", item.code);
                                            bundle.putString("lesson_editor", editor);

                                            NavController navController = NavHostFragment.findNavController(LessonsFragment.this);
                                            navController.navigate(R.id.nav_lesson, bundle);
                                        }
                                    });
                                }
                                tableRow.addView(textView);
                            }
                            lessonsResult.addView(tableRow);
                        }
                    }
                }
            }
        });

        viewModel.getFinishLessonsResult().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(stringFormState, binding.loading, null, afterFinish());
            }
        });
    }

    private Runnable afterFinish() {
        return new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putString("course_id", courseId);

                NavController navController = NavHostFragment.findNavController(LessonsFragment.this);
                navController.navigate(R.id.nav_sections, bundle);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
