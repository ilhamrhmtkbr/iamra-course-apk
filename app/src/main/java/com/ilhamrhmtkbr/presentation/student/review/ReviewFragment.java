package com.ilhamrhmtkbr.presentation.student.review;

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
import com.ilhamrhmtkbr.databinding.FragmentStudentReviewBinding;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;

import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReviewFragment extends Fragment {
    private FragmentStudentReviewBinding binding;
    private ReviewViewModel viewModel;
    private String courseId;
    private String formType;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

        if (getArguments() != null) {
            formType = getArguments().getString("form_type");
            courseId = getArguments().getString("course_id");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentStudentReviewBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupElement();
        setupListeners();
        setupViewModel();
    }

    private void setupElement() {
        binding.formType.setText(TextUtil.capitalize(formType));

        if (formType.equals("edit")) {
            binding.reviewDetail.setVisibility(VISIBLE);
            assert getArguments() != null;
            binding.courseTitle.setValue(getArguments().getString("course_title"));
            binding.courseDesc.setValue(getArguments().getString("course_desc"));
            binding.review.setValue(getArguments().getString("review"));
            binding.rating.setRating(Math.round(Integer.parseInt(getArguments().getString("rating")) / 2));
            binding.inputReview.setValue(getArguments().getString("review"));
        } else {
            binding.reviewDetail.setVisibility(GONE);
        }
    }

    private void setupListeners() {
        if (formType.equals("edit")) {
            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.modify(courseId, binding.inputReview.getValue(), String.valueOf(Math.round(binding.inputRating.getRating() * 2)));
                }
            });

            binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String message = getString(R.string.are_you_sure_you_want_to_delete_this) + " " + getString(R.string.review) + " ?";

                    DialogUtil.showWarning(requireContext(),
                            getString(R.string.confirm_delete), message, new DialogUtil.DialogCallback() {
                                @Override
                                public void onPositive() {
                                    assert getArguments() != null;
                                    viewModel.delete(getArguments().getString("review_id"));
                                }
                            });
                }
            });
        } else {
            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.store(courseId, binding.inputReview.getValue(), String.valueOf(Math.round(binding.inputRating.getRating() * 2)));
                }
            });
        }
    }

    private void setupViewModel() {
        viewModel.getValidationErrorsFrontend().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                showErrors(stringStringMap).run();
            }
        });

        viewModel.getReviewFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(stringFormState, binding.loading, showErrors(stringFormState.getValidationErrors()), handleAfterRequest());
            }
        });
    }

    private Runnable handleAfterRequest() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();

                NavController navController = NavHostFragment.findNavController(ReviewFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_review, true)
                        .build();
                navController.navigate(R.id.nav_reviews, null, navOptions);
                viewModel.refresh();
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

//                DialogUtil.showErrorSnackbar(binding.getRoot(), getString(R.string.validation_show_errors));

                for (Map.Entry<String, String> item : errors.entrySet()) {
                    String field = item.getKey();
                    String error = item.getValue();
                    switch (field) {
                        case "review":
                            binding.inputReview.getError().setVisibility(VISIBLE);
                            binding.inputReview.getError().setText(error);
                            break;
                        case "rating":
                            binding.errorRating.setVisibility(VISIBLE);
                            binding.errorRating.setText(error);
                            break;
                    }
                }
            }
        };
    }

    private void removeErrors() {
        binding.inputReview.getError().setText(null);
        binding.errorRating.setText(null);
        binding.inputReview.getError().setVisibility(GONE);
        binding.errorRating.setVisibility(GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
