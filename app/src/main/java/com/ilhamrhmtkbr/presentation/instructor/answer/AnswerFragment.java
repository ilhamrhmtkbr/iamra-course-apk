package com.ilhamrhmtkbr.presentation.instructor.answer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.databinding.FragmentInstructorAnswerBinding;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;

import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AnswerFragment extends Fragment {
    private FragmentInstructorAnswerBinding binding;
    private AnswerViewModel viewModel;
    private String formType;
    private String questionId;
    private String answerId;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            formType = getArguments().getString("form_type");
            assert formType != null;
            if (formType.equals("edit")) {
                answerId = getArguments().getString("answer_id");
            } else {
                questionId = getArguments().getString("question_id");
            }
        }
        viewModel = new ViewModelProvider(this).get(AnswerViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorAnswerBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupElements();
        setupButtonSubmit();
        observeViewModel();
    }

    private void setupElements() {
        assert getArguments() != null;

        binding.formType.setText(TextUtil.capitalize(getArguments().getString("form_type")));

        if (formType.equals("edit")) {
            binding.inputAnswer.setValue(getArguments().getString("answer"));
            binding.answerDetail.setVisibility(VISIBLE);
            binding.courseTitle.setValue(getArguments().getString("course_title"));
            binding.question.setValue(getArguments().getString("question"));
            binding.answer.setValue(getArguments().getString("answer"));
            binding.createdAt.setValue(getArguments().getString("created_at"));
        } else {
            binding.answerDetail.setVisibility(GONE);
        }
    }

    private void setupButtonSubmit() {
        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formType.equals("edit")) {
                    binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewModel.modify(
                                    binding.inputAnswer.getValue(),
                                    answerId
                            );
                        }
                    });
                } else {
                    binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewModel.store(
                                    binding.inputAnswer.getValue(),
                                    questionId
                            );
                        }
                    });
                }
            }
        });
    }

    private void observeViewModel() {
        viewModel.getValidationErrorsFrontend().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                showError(stringStringMap).run();
            }
        });

        viewModel.getAnswerFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> state) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(state, binding.loading, showError(state.getValidationErrors()), afterSuccess());
            }
        });
    }

    private Runnable showError(Map<String, String> errors) {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                if (errors.isEmpty()) {
                    return;
                }
                DialogUtil.showErrorSnackbar(binding.getRoot(), getString(R.string.validation_show_errors));
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    String field = entry.getKey();
                    String error = entry.getValue();

                    if (field.equals("answer")) {
                        binding.inputAnswer.getError().setText(error);
                        binding.inputAnswer.getError().setVisibility(VISIBLE);
                    }
                }
            }
        };
    }

    private void removeErrors() {
        binding.inputAnswer.getError().setText(null);
        binding.inputAnswer.getError().setVisibility(GONE);
    }

    private Runnable afterSuccess() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                viewModel.refreshAnswers();
                NavController navController = NavHostFragment.findNavController(AnswerFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_answer, true)
                        .build();
                navController.navigate(R.id.nav_answers, null, navOptions);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
