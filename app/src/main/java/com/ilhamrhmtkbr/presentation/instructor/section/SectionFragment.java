package com.ilhamrhmtkbr.presentation.instructor.section;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.text.InputType;
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
import com.ilhamrhmtkbr.databinding.FragmentInstructorSectionBinding;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;

import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SectionFragment extends Fragment {
    private FragmentInstructorSectionBinding binding;
    private SectionViewModel viewModel;
    private String formType;
    private String courseId;
    private String courseTitle;
    private String courseEditor;
    private String sectionId;
    private String sectionTitle;
    private String sectionOrderInCourse;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            formType = getArguments().getString("form_type");
            courseId = getArguments().getString("course_id");
            courseTitle = getArguments().getString("course_title");
            courseEditor = getArguments().getString("course_editor");
            assert formType != null;
            if (formType.equals("edit")) {
                sectionId = getArguments().getString("section_id");
                sectionTitle = getArguments().getString("section_title");
                sectionOrderInCourse = getArguments().getString("section_order");
            }
        }
        viewModel = new ViewModelProvider(this).get(SectionViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorSectionBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupElements();
        setupViewModels();
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
                            viewModel.delete(sectionId);
                        }
                    });
                }
            });

            binding.inputTitle.setValue(sectionTitle);
            binding.inputOrder.setValue(sectionOrderInCourse);

            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.modify(
                            sectionId,
                            courseId,
                            binding.inputTitle.getValue(),
                            binding.inputOrder.getValue());
                }
            });
        } else {
            binding.buttonDelete.setVisibility(GONE);

            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.store(
                            courseId,
                            binding.inputTitle.getValue(),
                            binding.inputOrder.getValue());
                }
            });
        }
    }

    private void setupViewModels() {
        viewModel.getSectionFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
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
                        case "order_in_course":
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
        binding.inputOrder.getError().setText(null);
        binding.inputOrder.getError().setVisibility(View.GONE);
    }

    private Runnable handleAfterRequest() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                viewModel.getSectionsRepository().fetch(courseId);

                Bundle bundle = new Bundle();
                bundle.putString("course_id", courseId);
                bundle.putString("course_title", courseTitle);
                bundle.putString("course_editor", courseEditor);

                NavController navController = NavHostFragment.findNavController(SectionFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_section, true)
                        .build();
                navController.navigate(R.id.nav_sections, bundle, navOptions);

            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
