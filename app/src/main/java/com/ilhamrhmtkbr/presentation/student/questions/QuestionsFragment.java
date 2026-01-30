package com.ilhamrhmtkbr.presentation.student.questions;

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
import com.ilhamrhmtkbr.domain.model.student.Question;
import com.ilhamrhmtkbr.presentation.utils.adapter.PaginationAdapter;
import com.ilhamrhmtkbr.databinding.FragmentStudentQuestionsBinding;
import com.ilhamrhmtkbr.presentation.utils.helper.RecyclerViewsUtil;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QuestionsFragment extends Fragment {
    private FragmentStudentQuestionsBinding binding;
    private QuestionsViewModel viewModel;
    private QuestionsAdapter questionsAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(QuestionsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentQuestionsBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupAdapter();
        setupListeners();
        setupSwipeRefresh();
        setupPagination();
        setupViewModel();
    }

    private void setupAdapter() {
        questionsAdapter = new QuestionsAdapter(new QuestionsAdapter.onClickListeners() {
            @Override
            public void detail(String questionId, String courseId) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "edit");
                bundle.putString("question_id", questionId);
                bundle.putString("course_id", courseId);

                NavController navController = NavHostFragment.findNavController(QuestionsFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_questions, false)
                        .build();
                navController.navigate(R.id.nav_question, bundle, navOptions);
            }
        });
        binding.rv.addItemDecoration(new RecyclerViewsUtil.VerticalSpaceItemDecoration(33));
        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rv.setAdapter(questionsAdapter);
    }

    private void setupPagination() {
        PaginationAdapter paginationAdapter = new PaginationAdapter(new PaginationAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(String url) {
                Uri uri = Uri.parse(url);
                String page = uri.getQueryParameter("page");
                viewModel.refreshQuestions(page, viewModel.getSort().getValue());
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
                viewModel.refreshQuestions("1", "desc");
            }
        });
    }

    private void setupListeners() {
        binding.filterNew.setOnClickListener(v -> {
            viewModel.getSort().setValue("desc");
            viewModel.refreshQuestions("1", "desc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.blue_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.text_color));

        });

        binding.filterOld.setOnClickListener(v -> {
            viewModel.getSort().setValue("asc");
            viewModel.refreshQuestions("1", "asc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.text_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.blue_color));

        });
    }

    private void setupViewModel() {
        viewModel.getQuestions().observe(getViewLifecycleOwner(), new Observer<Resource<List<Question>>>() {
            @Override
            public void onChanged(Resource<List<Question>> listResource) {
                LoadingUtil.setupWithSwipeRefresh(listResource.isLoading(), binding.loading, binding.swipeRefresh);
                if (listResource.isSuccess()) {
                    if (listResource.getData() == null || listResource.getData().isEmpty()) {
                        binding.noData.setVisibility(VISIBLE);
                        binding.data.setVisibility(GONE);
                    } else {
                        binding.noData.setVisibility(GONE);
                        binding.data.setVisibility(VISIBLE);
                        questionsAdapter.setData(listResource.getData());
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
