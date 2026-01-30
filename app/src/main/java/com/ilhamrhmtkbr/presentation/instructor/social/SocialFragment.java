package com.ilhamrhmtkbr.presentation.instructor.social;

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
import com.ilhamrhmtkbr.databinding.FragmentInstructorSocialBinding;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.core.utils.ui.TextUtil;

import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SocialFragment extends Fragment {
    private FragmentInstructorSocialBinding binding;
    private SocialViewModel viewModel;
    private String formType;
    private String socialId;
    private String urlLink;
    private String displayName;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            formType = getArguments().getString("form_type");
            assert formType != null;
            if (formType.equals("edit")) {
                socialId = getArguments().getString("id");
                urlLink = getArguments().getString("url_link");
                displayName = getArguments().getString("display_name");
            }
        }
        viewModel = new ViewModelProvider(this).get(SocialViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentInstructorSocialBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupElements();
        setupViewModels();
    }

    private void setupElements() {
        binding.formType.setText(TextUtil.capitalize(formType));
        if (formType.equals("edit")) {
            binding.buttonDelete.setVisibility(VISIBLE);
            binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DialogUtil.showWarning(
                            requireContext(),
                            getString(R.string.delete),
                            getString(R.string.are_you_sure_you_want_to_delete_this) + " " + getString(R.string.section) + " ?",
                            new DialogUtil.DialogCallback() {
                                @Override
                                public void onPositive() {
                                    viewModel.delete(socialId);
                                }
                            });
                }
            });

            binding.inputUrl.setValue(urlLink);
            binding.inputDisplayName.setValue(displayName);

            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.modify(
                            socialId,
                            binding.inputDisplayName.getValue(),
                            binding.inputUrl.getValue()
                    );
                }
            });
        } else {
            binding.buttonDelete.setVisibility(GONE);
            binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewModel.store(
                            binding.inputDisplayName.getValue(),
                            binding.inputUrl.getValue()
                    );
                }
            });
        }
    }

    private void setupViewModels() {
        viewModel.getValidationErrorsFrontend().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                showErrors(stringStringMap).run();
            }
        });

        viewModel.getSocialsFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(stringFormState, binding.loading, showErrors(stringFormState.getValidationErrors()), handleAfterRequest());
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
                        case "url_link":
                            binding.inputUrl.getError().setVisibility(View.VISIBLE);
                            binding.inputUrl.getError().setText(error);
                            break;
                        case "display_name":
                            binding.inputDisplayName.getError().setVisibility(View.VISIBLE);
                            binding.inputDisplayName.getError().setText(error);
                            break;
                    }
                }
            }
        };
    }

    private void removeErrors() {
        binding.inputUrl.getError().setText(null);
        binding.inputUrl.getError().setVisibility(View.GONE);
        binding.inputDisplayName.getError().setText(null);
        binding.inputDisplayName.getError().setVisibility(View.GONE);
    }

    private Runnable handleAfterRequest() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                viewModel.getSocialsRepository().fetch();
                NavController navController = NavHostFragment.findNavController(SocialFragment.this);
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.nav_social, true)
                        .build();
                navController.navigate(R.id.nav_socials, null, navOptions);
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
