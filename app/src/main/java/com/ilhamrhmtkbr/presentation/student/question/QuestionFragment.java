package com.ilhamrhmtkbr.presentation.student.question;

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
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.databinding.FragmentStudentQuestionBinding;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.domain.model.student.Question;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;

import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QuestionFragment extends Fragment {
    private FragmentStudentQuestionBinding binding;
    private String type;
    private String courseId;
    private String questionId;
    private QuestionViewModel viewModel;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(QuestionViewModel.class);

        if (getArguments() != null) {
            type = getArguments().getString("type");
            courseId = getArguments().getString("course_id");

            if (getArguments().getString("question_id") != null) {
                questionId = getArguments().getString("question_id");
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentQuestionBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupElements();
        setupListeners();
        setupViewModel();
    }

    private void setupElements() {
        if (type.equals("add")) {
            binding.questionFormType.setText(getString(R.string.add));
            binding.questionDetail.setVisibility(GONE);
        } else {
            binding.questionFormType.setText(getString(R.string.edit));
            binding.questionDetail.setVisibility(VISIBLE);
        }
    }

    private void setupListeners() {
        if (type.equals("add")) {
            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.store(binding.inputQuestion.getValue(), courseId);
                }
            });
        } else {
            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.modify(questionId, courseId, binding.inputQuestion.getValue());
                }
            });

            binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = getString(R.string.are_you_sure_you_want_to_delete_this) + " " + getString(R.string.question) + " ?";

                    DialogUtil.showWarning(requireContext(), getString(R.string.confirm_delete), message, new DialogUtil.DialogCallback() {
                        @Override
                        public void onPositive() {
                            viewModel.delete(questionId);
                        }
                    });
                }
            });
        }
    }

    private void setupViewModel() {
        if (type.equals("edit")) {
            viewModel.show(questionId);

            viewModel.getQuestion().observe(getViewLifecycleOwner(), new Observer<Resource<Question>>() {
                @Override
                public void onChanged(Resource<Question> questionResource) {
                    LoadingUtil.setup(questionResource.isLoading(), binding.loading);

                    if (questionResource.getData() != null) {
                        binding.questionCourseTitle.setValue(questionResource.getData().getCourseTitle());
                        binding.questionQuestion.setValue(questionResource.getData().getQuestion());
                        binding.questionAnswer.setValue(questionResource.getData().getAnswer());
                        binding.questionAnswerCreatedAt.setValue(questionResource.getData().getCreatedAt());
                        binding.inputQuestion.setValue(questionResource.getData().getQuestion());
                    }
                }
            });
        }

        viewModel.getQuestionFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                LoadingUtil.setup(stringFormState.isLoading(), binding.loading);
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(stringFormState, binding.loading, showErrors(stringFormState.getValidationErrors()), handleAfterRequest());
            }
        });

        viewModel.getValidationErrorsFrontend().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                showErrors(stringStringMap).run();
            }
        });
    }

    private Runnable handleAfterRequest() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                viewModel.refreshQuestions();
                NavController navController = NavHostFragment.findNavController(QuestionFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_question, true)
                        .build();
                navController.navigate(R.id.nav_questions, null, navOptions);
            }
        };
    }

    private Runnable showErrors(Map<String, String> errors) {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                if (errors.isEmpty()) {
                    return;
                }

                DialogUtil.showErrorSnackbar(binding.getRoot(), getString(R.string.validation_show_errors));

                for (Map.Entry<String, String> item : errors.entrySet()) {
                    String field = item.getKey();
                    String error = item.getValue();

                    if (field.equals("question")) {
                        binding.inputQuestion.getError().setVisibility(VISIBLE);
                        binding.inputQuestion.getError().setText(error);
                    }
                }
            }
        };
    }

    private void removeErrors() {
        binding.inputQuestion.getError().setText(null);
        binding.inputQuestion.getError().setVisibility(GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
        type = null;
        courseId = null;
        questionId = null;
    }
}
