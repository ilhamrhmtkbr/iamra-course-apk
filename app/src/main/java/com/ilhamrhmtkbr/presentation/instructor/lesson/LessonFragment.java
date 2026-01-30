package com.ilhamrhmtkbr.presentation.instructor.lesson;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
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
import com.ilhamrhmtkbr.databinding.FragmentInstructorLessonBinding;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;

import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LessonFragment extends Fragment {
    private FragmentInstructorLessonBinding binding;
    private LessonViewModel viewModel;
    private String formType;
    private String sectionId;
    private String lessonId;
    private String lessonTitle;
    private String lessonDesc;
    private String lessonCode;
    private String lessonOrder;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            formType = getArguments().getString("form_type");
            sectionId = getArguments().getString("section_id");
            assert formType != null;
            if (formType.equals("edit")) {
                lessonId = getArguments().getString("lesson_id");
                lessonTitle = getArguments().getString("lesson_title");
                lessonDesc = getArguments().getString("lesson_description");
                lessonCode = getArguments().getString("lesson_code");
                lessonOrder = getArguments().getString("lesson_order");
            }
        }
        viewModel = new ViewModelProvider(this).get(LessonViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorLessonBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupElements();
        observeViewModels();
    }

    private void setupElements() {
        binding.formType.setText(TextUtil.capitalize(formType));
        binding.inputOrder.getInput().setInputType(InputType.TYPE_CLASS_NUMBER);

        if (formType.equals("edit")) {
            binding.buttonDelete.setVisibility(VISIBLE);

            binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtil.showWarning(requireContext(), getString(R.string.delete), getString(R.string.are_you_sure_you_want_to_delete_this) + " " + getString(R.string.section) + " ?", new DialogUtil.DialogCallback() {
                        @Override
                        public void onPositive() {
                            viewModel.delete(sectionId, lessonId);
                        }
                    });
                }
            });

            binding.inputTitle.setValue(lessonTitle);
            binding.inputDescription.setValue(lessonDesc);

            byte[] decodedBytes = Base64.decode(lessonCode, Base64.DEFAULT);
            String cleanCode = new String(decodedBytes);

            binding.inputCode.setText(cleanCode);

            binding.inputOrder.setValue(lessonOrder);

            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.modify(
                            lessonId,
                            sectionId,
                            binding.inputTitle.getValue(),
                            binding.inputDescription.getValue(),
                            binding.inputCode.getText().toString(),
                            binding.inputOrder.getValue()
                    );
                }
            });
        } else {
            binding.buttonDelete.setVisibility(GONE);

            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.store(
                            sectionId,
                            binding.inputTitle.getValue(),
                            binding.inputDescription.getValue(),
                            binding.inputCode.getText().toString(),
                            binding.inputOrder.getValue()
                    );
                }
            });
        }
    }

    private void observeViewModels() {
        viewModel.getLessonFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
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

                    switch (field) {
                        case "title":
                            binding.inputTitle.getError().setVisibility(View.VISIBLE);
                            binding.inputTitle.getError().setText(error);
                            break;
                        case "description":
                            binding.inputDescription.getError().setVisibility(View.VISIBLE);
                            binding.inputDescription.getError().setText(error);
                            break;
                        case "code":
                            binding.errorCode.setVisibility(View.VISIBLE);
                            binding.errorCode.setText(error);
                            break;
                        case "order_in_section":
                            binding.inputOrder.getError().setVisibility(View.VISIBLE);
                            binding.inputOrder.getError().setText(error);
                            break;
                    }
                }
            }
        };
    }

    private void removeErrors() {
        binding.inputTitle.getError().setText(null);
        binding.inputTitle.getError().setVisibility(View.GONE);
        binding.inputDescription.getError().setText(null);
        binding.inputDescription.getError().setVisibility(View.GONE);
        binding.errorCode.setText(null);
        binding.errorCode.setVisibility(View.GONE);
        binding.inputOrder.getError().setText(null);
        binding.inputOrder.getError().setVisibility(View.GONE);
    }

    private Runnable handleAfterRequest() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                Bundle bundle = new Bundle();
                bundle.putString("section_id", sectionId);

                NavController navController = NavHostFragment.findNavController(LessonFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_lesson, true)
                        .build();
                navController.navigate(R.id.nav_lessons, bundle, navOptions);
                viewModel.getLessonsRepository().fetch("1", sectionId);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
