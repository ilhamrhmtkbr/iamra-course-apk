package com.ilhamrhmtkbr.presentation.auth.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;
import com.ilhamrhmtkbr.presentation.auth.RecaptchaValidation;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;
import com.ilhamrhmtkbr.presentation.utils.helper.LoadingUtil;
import com.ilhamrhmtkbr.presentation.guest.GuestActivity;
import com.ilhamrhmtkbr.databinding.FragmentUserAuthLoginBinding;
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthLoginRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthLoginWithGoogleRequest;
import com.ilhamrhmtkbr.presentation.auth.register.RegisterFragment;
import com.ilhamrhmtkbr.core.utils.tools.RedirectUtil;
import com.ilhamrhmtkbr.presentation.member.MemberActivity;

import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment implements RecaptchaValidation {
    private static final String TAG = "LoginFragment";
    private FragmentUserAuthLoginBinding binding;
    private LoginViewModel viewModel;
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityResultLauncher<Intent> googleSingInLauncher;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        googleSingInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult activityResult) {
                        if (activityResult.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = activityResult.getData();
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                            handleSignInResult(task);
                        } else if (activityResult.getResultCode() == Activity.RESULT_CANCELED) {
                            Toast.makeText(requireContext(), R.string.login_cancelled, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), R.string.login_failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        binding = FragmentUserAuthLoginBinding.inflate(getLayoutInflater(), viewGroup, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        setupButtonOnclick();
        observeViewModel();
        observeValidationFrontend();
    }

    private void setupButtonOnclick() {
        binding.buttonSeeCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RedirectUtil.redirectToActivity(requireContext(), GuestActivity.class);
            }
        });

        binding.navToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterFragment fragment = new RegisterFragment();
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_content, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserAuthLoginRequest request = new UserAuthLoginRequest(
                        binding.inputUsername.getText().toString(),
                        binding.inputPassword.getText().toString(),
                        null
                );

                if (viewModel.isValidValue(request)) {
                    verifyRecaptcha(requireActivity(), binding.loading, new RecaptchaCallback() {
                        @Override
                        public void onSuccess(String token) {
                            UserAuthLoginRequest request = new UserAuthLoginRequest(
                                    binding.inputUsername.getText().toString(),
                                    binding.inputPassword.getText().toString(),
                                    token
                            );
                            viewModel.login(request);
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.e(TAG, "reCAPTCHA verification failed: " + errorMessage);
                        }
                    });
                }
            }
        });

        binding.buttonLoginWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
    }

    private void observeViewModel() {
        viewModel.getLoginState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(stringFormState, binding.loading, showError(stringFormState.getValidationErrors()), afterSuccess());
            }
        });

        viewModel.getUserData().observe(getViewLifecycleOwner(), new Observer<FormState<UserResponse>>() {
            @Override
            public void onChanged(FormState<UserResponse> userResponseFormState) {
                LoadingUtil.setup(userResponseFormState.isLoading(), binding.loading);
                if (userResponseFormState.isSuccess()) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RedirectUtil.redirectToActivity(requireContext(), MemberActivity.class);
                        }
                    }, 1300);
                }
            }
        });
    }

    private void signInWithGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = mGoogleSignInClient.getSignInIntent();
                googleSingInLauncher.launch(intent);
            }
        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {
                String email = account.getEmail();
                String idToken = account.getIdToken();

                Log.d(TAG, "Google email: " + email);
                Log.d(TAG, "ID Token: " + (idToken != null ? "received" : "null"));

                if (email == null) {
                    Toast.makeText(requireContext(), R.string.login_with_google_failed_to_get_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                verifyRecaptcha(requireActivity(), binding.loading, new RecaptchaCallback() {
                    @Override
                    public void onSuccess(String token) {
                        UserAuthLoginWithGoogleRequest request = new UserAuthLoginWithGoogleRequest();
                        request.email = email;
                        request.idToken = idToken;
                        request.captcha = token;

                        Toast.makeText(requireContext(), getString(R.string.login_with_google_login_with) + email, Toast.LENGTH_SHORT).show();
                        viewModel.loginWithGoogle(request);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(requireContext(), "reCAPTCHA failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(requireContext(), R.string.login_with_google_google_account_not_found, Toast.LENGTH_SHORT).show();
            }

        } catch (ApiException e) {
            // Handle specific error codes
            switch (e.getStatusCode()) {
                case 12501: // User cancelled
                    Toast.makeText(requireContext(), R.string.login_cancelled, Toast.LENGTH_SHORT).show();
                    break;
                case 12500: // Sign in failed (network issue, etc)
                    Toast.makeText(requireContext(), R.string.login_with_google_failed_to_connect_to_google_check_your_internet_connection, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(requireContext(), R.string.login_cancelled, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void observeValidationFrontend() {
        viewModel.getValidationFrontend().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> maps) {
                showError(maps).run();
            }
        });
    }

    private Runnable showError(Map<String, String> errors) {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                if(errors.isEmpty()) {
                    return;
                }
                DialogUtil.showErrorSnackbar(binding.getRoot(), getString(R.string.validation_show_errors));
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    String field = entry.getKey();
                    String error = entry.getValue();
                    switch (field) {
                        case "username":
                            binding.inputLayoutUsername.setError(error);
                            break;
                        case "password":
                            binding.inputLayoutPassword.setError(error);
                            break;
                        default:
                            Toast.makeText(requireContext(), field + ": " + error, Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        };
    }

    private void removeErrors() {
        binding.inputLayoutUsername.setError(null);
        binding.inputLayoutUsername.setErrorEnabled(false);
        binding.inputLayoutPassword.setError(null);
        binding.inputLayoutPassword.setErrorEnabled(false);
    }

    private Runnable afterSuccess() {
        return new Runnable() {
            @Override
            public void run() {
                removeErrors();
                viewModel.initUserData();
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}