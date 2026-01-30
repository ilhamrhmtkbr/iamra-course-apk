package com.ilhamrhmtkbr.presentation.instructor.answers;

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
import com.ilhamrhmtkbr.domain.model.instructor.Answer;
import com.ilhamrhmtkbr.presentation.utils.adapter.PaginationAdapter;
import com.ilhamrhmtkbr.databinding.FragmentInstructorAnswersBinding;
import com.ilhamrhmtkbr.presentation.utils.helper.RecyclerViewsUtil;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AnswersFragment extends Fragment {
    private FragmentInstructorAnswersBinding binding;
    private AnswersViewModel viewModel;
    private AnswersAdapter answersAdapter;
    private PaginationAdapter paginationAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(AnswersViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorAnswersBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupAnswers();
        setupPagination();
        setupListeners();
        observeViewModels();
    }

    private void setupAnswers() {
        answersAdapter = new AnswersAdapter(new AnswersAdapter.onClickListener() {
            @Override
            public void edit(Answer data) {
                Bundle bundle = new Bundle();
                bundle.putString("course_title", data.getTitle());
                bundle.putString("question", data.getQuestion());
                bundle.putString("created_at", data.getQuestionCreatedAt());
                bundle.putString("student", data.getStudent());
                if (data.getAnswer() != null) {
                    bundle.putString("form_type", "edit");
                    bundle.putString("answer", data.getAnswer());
                    bundle.putString("answer_id", data.getAnswerId());
                } else {
                    bundle.putString("form_type", "add");
                    bundle.putString("question_id", data.getQuestionId());
                }


                NavController navController = NavHostFragment.findNavController(AnswersFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_answers, false)
                        .build();
                navController.navigate(R.id.nav_answer, bundle, navOptions);
            }
        });
        binding.rv.addItemDecoration(new RecyclerViewsUtil.VerticalSpaceItemDecoration(33));
        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rv.setAdapter(answersAdapter);
    }

    private void setupListeners() {
        binding.swipeRefresh.setColorSchemeResources(R.color.blue_color);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.refreshAnswers("1", "desc");
            }
        });

        binding.filterNew.setOnClickListener(v -> {
            viewModel.getSort().setValue("desc");
            viewModel.refreshAnswers("1", "desc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.blue_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.text_color));

        });

        binding.filterOld.setOnClickListener(v -> {
            viewModel.getSort().setValue("asc");
            viewModel.refreshAnswers("1", "asc");
            binding.filterNew.setTextColor(requireContext().getColor(R.color.text_color));
            binding.filterOld.setTextColor(requireContext().getColor(R.color.blue_color));

        });
    }

    private void setupPagination() {
        paginationAdapter = new PaginationAdapter(new PaginationAdapter.OnPageClickListener() {
            @Override
            public void onPageClick(String url) {
                Uri uri = Uri.parse(url);
                String page = uri.getQueryParameter("page");
                viewModel.refreshAnswers(page, viewModel.getSort().getValue());
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

    private void observeViewModels() {
        viewModel.getAnswers().observe(getViewLifecycleOwner(), new Observer<Resource<List<Answer>>>() {
            @Override
            public void onChanged(Resource<List<Answer>> listResource) {
                LoadingUtil.setupWithSwipeRefresh(listResource.isLoading(), binding.loading, binding.swipeRefresh);
                if (listResource.isSuccess()) {
                    if (listResource.getData() != null) {
                        binding.noData.setVisibility(GONE);
                        binding.data.setVisibility(VISIBLE);
                        answersAdapter.setData(listResource.getData());
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
