package com.ilhamrhmtkbr.presentation.member.email;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ilhamrhmtkbr.BuildConfig;
import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.core.auth.AuthStateManager;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.presentation.auth.AuthActivity;
import com.ilhamrhmtkbr.presentation.utils.component.ButtonSubmit;
import com.ilhamrhmtkbr.presentation.utils.component.InputText;
import com.ilhamrhmtkbr.databinding.FragmentUserMemberEmailBinding;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateEmailRequest;
import com.ilhamrhmtkbr.data.remote.websocket.CustomWebSocketListener;
import com.ilhamrhmtkbr.core.utils.notif.DialogUtil;
import com.ilhamrhmtkbr.presentation.utils.tools.FormStateHandler;

import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

@AndroidEntryPoint
public class EmailFragment extends Fragment {
    private static final String TAG = "EmailFragment";
    private static final String PREFS_NAME = "email_verification";
    private static final int VERIFICATION_CHECK_INTERVAL = 5000; // 5 seconds

    private InputText inputEmail;
    private EmailViewModel viewModel;
    private ButtonSubmit buttonSubmitEmail;
    private FragmentUserMemberEmailBinding binding;

    private WebSocket webSocket;
    private int retryCount = 0;
    private final int maxRetries = 3;
    private final int reconnectInterval = 3000;
    private boolean isConnecting = false;
    private boolean connectionAttempted = false;
    private boolean isWaitingVerification = false;

    private final Handler retryHandler = new Handler(Looper.getMainLooper());
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Handler verificationCheckHandler = new Handler(Looper.getMainLooper());

    @Inject
    AuthStateManager authStateManager;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        viewModel = new ViewModelProvider(requireActivity()).get(EmailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle savedInstance) {
        binding = FragmentUserMemberEmailBinding.inflate(getLayoutInflater());
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

            // ✅ CEK STATUS VERIFIKASI SAAT RESUME
            checkVerificationStatusOnResume();
        }
    }

    private void setupBinding() {
        inputEmail = binding.inputEmail;
        buttonSubmitEmail = binding.buttonSubmitEmail;
    }

    private void setupFormData() {
        inputEmail.setValue(authStateManager.getCurrentUserEmail());
    }

    private void setupListeners() {
        buttonSubmitEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberUpdateEmailRequest request = new MemberUpdateEmailRequest(
                        inputEmail.getValue()
                );
                viewModel.request(request);
            }
        });
    }

    private void setupViewModel() {
        viewModel.getEmailFormState().observe(getViewLifecycleOwner(), new Observer<FormState<String>>() {
            @Override
            public void onChanged(FormState<String> stringFormState) {
                FormStateHandler<String> formStateHandler = new FormStateHandler<>();
                formStateHandler.handle(stringFormState, binding.loading, showErrors(stringFormState.getValidationErrors()), afterSuccess());

                if (stringFormState.isSuccess()) {
                    Log.d(TAG, "Email update successful, connecting WebSocket");

                    // ✅ SET FLAG MENUNGGU VERIFIKASI
                    isWaitingVerification = true;
                    saveVerificationWaitingState(true);

                    connectionAttempted = false;
                    retryCount = 0;
                    connectWebSocket();

                    // ✅ START PERIODIC CHECK (backup jika WebSocket gagal)
                    startPeriodicVerificationCheck();
                }
            }
        });

        viewModel.getValidationErrorsFrontend().observe(getViewLifecycleOwner(), new Observer<Map<String, String>>() {
            @Override
            public void onChanged(Map<String, String> stringStringMap) {
                showErrors(stringStringMap).run();
            }
        });
    }


    private void checkVerificationStatusOnResume() {
        boolean isWaiting = getVerificationWaitingState();

        if (isWaiting) {
            Log.d(TAG, "Detected pending verification on resume, checking status...");
            isWaitingVerification = true;

            // Langsung cek status
            checkVerificationStatus();

            // Dan mulai periodic check
            startPeriodicVerificationCheck();
        }
    }

    // ✅ METODE BARU: CEK STATUS VERIFIKASI
    private void checkVerificationStatus() {
        if (!isAdded() || requireContext() == null) {
            return;
        }

        Log.d(TAG, "Checking email verification status...");

        // Tunggu sebentar untuk data update, lalu cek
        mainHandler.postDelayed(() -> {
            if (!isAdded()) return;

            String emailVerifiedAt = authStateManager.getCurrentUserEmailVerifiedAt();

            Log.d(TAG, "Verification status check - email_verified_at: " + emailVerifiedAt);

            if (emailVerifiedAt != null &&
                    !emailVerifiedAt.isEmpty() &&
                    !emailVerifiedAt.equals("null")) {

                // ✅ EMAIL SUDAH TERVERIFIKASI
                Log.d(TAG, "Email verified detected!");
                isWaitingVerification = false;
                saveVerificationWaitingState(false);
                verificationCheckHandler.removeCallbacksAndMessages(null);

                showVerificationSuccessDialog();
            } else {
                Log.d(TAG, "Email not yet verified, will continue monitoring");
            }
        }, 1000); // Delay 1 detik untuk tunggu AuthManager.initUserData selesai
    }

    // ✅ METODE BARU: PERIODIC CHECK
    private void startPeriodicVerificationCheck() {
        // Cancel existing check
        verificationCheckHandler.removeCallbacksAndMessages(null);

        if (!isWaitingVerification) {
            return;
        }

        Log.d(TAG, "Starting periodic verification check");

        verificationCheckHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isWaitingVerification && isAdded()) {
                    checkVerificationStatus();

                    // Schedule next check
                    if (isWaitingVerification) {
                        verificationCheckHandler.postDelayed(this, VERIFICATION_CHECK_INTERVAL);
                    }
                }
            }
        }, VERIFICATION_CHECK_INTERVAL);
    }

    // ✅ SIMPAN STATE DI SHAREDPREFERENCES
    private void saveVerificationWaitingState(boolean waiting) {
        if (!isAdded() || requireContext() == null) return;

        try {
            SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String key = "waiting_verification_" + authStateManager.getCurrentUsername();
            prefs.edit().putBoolean(key, waiting).apply();

            Log.d(TAG, "Saved verification waiting state: " + waiting + " for user: " + authStateManager.getCurrentUsername());
        } catch (Exception e) {
            Log.e(TAG, "Error saving verification state", e);
        }
    }

    private boolean getVerificationWaitingState() {
        if (!isAdded() || requireContext() == null) return false;

        try {
            SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String key = "waiting_verification_" + authStateManager.getCurrentUsername();
            return prefs.getBoolean(key, false);
        } catch (Exception e) {
            Log.e(TAG, "Error reading verification state", e);
            return false;
        }
    }

    private void connectWebSocket() {
        String username = authStateManager.getCurrentUsername();
        if (username.isEmpty()) {
            Log.e(TAG, "Username is null or empty, cannot connect WebSocket");
            return;
        }

        if (isConnecting || (webSocket != null &&
                (webSocket.request().url().toString().equals(BuildConfig.REVERB_USER_URL)))) {
            Log.d(TAG, "WebSocket already connecting or connected");
            return;
        }

        if (connectionAttempted) {
            Log.d(TAG, "Connection already attempted");
            return;
        }

        connectionAttempted = true;
        isConnecting = true;

        Log.d(TAG, "Attempting to connect WebSocket for user: " + username);

        closeWebSocket();

        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                    .build();

            Request request = new Request.Builder()
                    .url(BuildConfig.REVERB_USER_URL)
                    .build();

            CustomWebSocketListener listener = new CustomWebSocketListener(new CustomWebSocketListener.WebSocketHandler() {
                @Override
                public void onVerified() {
                    mainHandler.post(() -> {
                        Log.d(TAG, "Verification received via WebSocket");
                        isWaitingVerification = false;
                        saveVerificationWaitingState(false);
                        verificationCheckHandler.removeCallbacksAndMessages(null);
                        showVerificationSuccessDialog();
                    });
                }

                @Override
                public void onConnectionFailed() {
                    Log.e(TAG, "WebSocket connection failed");
                    isConnecting = false;
                    retryConnection();
                }

                @Override
                public void onRetryNeeded() {
                    Log.d(TAG, "WebSocket retry needed");
                    isConnecting = false;
                    retryConnection();
                }
            }, username, requireContext());

            webSocket = client.newWebSocket(request, listener);

            mainHandler.postDelayed(() -> {
                if (isConnecting && (webSocket == null || !listener.isSubscribed())) {
                    Log.w(TAG, "WebSocket connection timeout");
                    isConnecting = false;
                    retryConnection();
                }
            }, 15000);

        } catch (Exception e) {
            Log.e(TAG, "Error creating WebSocket connection", e);
            isConnecting = false;
            retryConnection();
        }
    }

    private void showVerificationSuccessDialog() {
        if (!isAdded() || requireContext() == null) {
            return;
        }

        DialogUtil.showSuccess(requireContext(),
                "Email Verification Success",
                "Your email has been verified successfully. You will be logged out. Please Login Again",
                new DialogUtil.DialogCallback() {
                    @Override
                    public void onPositive() {
                        authStateManager.logout();
                        Intent intent = new Intent(requireContext(), AuthActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
    }

    private void retryConnection() {
        if (retryCount < maxRetries && isAdded()) {
            retryCount++;
            Log.d(TAG, "Retrying WebSocket connection... (" + retryCount + "/" + maxRetries + ")");

            retryHandler.postDelayed(() -> {
                if (isAdded()) {
                    connectionAttempted = false;
                    connectWebSocket();
                }
            }, reconnectInterval);
        } else {
            Log.d(TAG, "Max retries reached, relying on periodic check");
            // Periodic check akan tetap jalan sebagai backup
        }
    }

    private void closeWebSocket() {
        if (webSocket != null) {
            Log.d(TAG, "Closing existing WebSocket connection");
            webSocket.close(1000, "Creating new connection");
            webSocket = null;
        }
    }

    private void cleanup() {
        Log.d(TAG, "Cleaning up WebSocket resources");

        retryHandler.removeCallbacksAndMessages(null);
        mainHandler.removeCallbacksAndMessages(null);
        verificationCheckHandler.removeCallbacksAndMessages(null);

        closeWebSocket();

        isConnecting = false;
        connectionAttempted = false;
        retryCount = 0;
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
                        case "email":
                            inputEmail.getError().setVisibility(View.VISIBLE);
                            inputEmail.getError().setText(error);
                            break;
                    }
                }
            }
        };
    }

    private void removeErrors() {
        inputEmail.getError().setVisibility(View.GONE);
        inputEmail.getError().setText(null);
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
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Fragment started");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Fragment stopped");
        cleanup();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "Fragment view destroyed");

        cleanup();

        binding = null;
        inputEmail = null;
        buttonSubmitEmail = null;
    }
}