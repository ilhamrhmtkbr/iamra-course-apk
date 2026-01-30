package com.ilhamrhmtkbr.presentation.member.authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.presentation.utils.component.ButtonSubmit;
import com.ilhamrhmtkbr.presentation.utils.component.InputText;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.databinding.FragmentUserMemberAuthenticationBinding;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateAuthenticationRequest;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;

import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AuthenticationFragment extends Fragment {
    private FragmentUserMemberAuthenticationBinding binding;
    private AuthenticationViewModel viewModel;
    private ButtonSubmit buttonSubmitAuthentication;
    private InputText inputUsername, inputOldPassword, inputNewPassword;
    @Inject
    AuthStateManager authStateManager;

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(requireActivity()).get(AuthenticationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstance) {
        binding = FragmentUserMemberAuthenticationBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        if (authStateManager.isLoggedIn()) {
            setupBinding();
            setupFormData();
            setupListeners();
            setupViewModel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (authStateManager.isLoggedIn()) {
            setupBinding();
            setupFormData();
            setupListeners();
            setupViewModel();
        }
    }

    private void setupBinding() {
        inputUsername = binding.inputUsername;
        inputOldPassword = binding.inputOldPassword;
        inputNewPassword = binding.inputNewPassword;
        buttonSubmitAuthentication = binding.buttonSubmitAuthentication;
    }

    private void setupFormData() {
        inputUsername.setValue(authStateManager.getCurrentUsername());
    }

    private void setupListeners() {
        buttonSubmitAuthentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberUpdateAuthenticationRequest request = new MemberUpdateAuthenticationRequest(
                        inputUsername.getValue(),
                        inputOldPassword.getValue(),
                        inputNewPassword.getValue()
                );

                viewModel.request(request);
            }
        });
    }

    private void setupViewModel() {
        viewModel.getAuthenticationFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(stringFormState, binding.loading, showErrors(stringFormState.getValidationErrors()), afterSuccess());
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
                        case "username":
                            inputUsername.getError().setVisibility(View.VISIBLE);
                            inputUsername.getError().setText(error);
                            break;
                        case "old_password":
                            inputOldPassword.getError().setVisibility(View.VISIBLE);
                            inputOldPassword.getError().setText(error);
                            break;
                        case "new_password":
                            inputNewPassword.getError().setVisibility(View.VISIBLE);
                            inputNewPassword.getError().setText(error);
                            break;
                    }
                }
            }
        };
    }

    private void removeErrors() {
        inputUsername.getError().setText(null);
        inputUsername.getError().setVisibility(View.GONE);
        inputOldPassword.getError().setText(null);
        inputOldPassword.getError().setVisibility(View.GONE);
        inputNewPassword.getError().setText(null);
        inputNewPassword.getError().setVisibility(View.GONE);
    }

    private Runnable afterSuccess() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                viewModel.refreshUserData();
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel = null;
        buttonSubmitAuthentication = null;
        inputUsername = null;
        inputOldPassword = null;
        inputNewPassword = null;
    }
}
