package com.ilhamrhmtkbr.presentation.auth.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;
import com.ilhamrhmtkbr.domain.repository.MemberRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthLoginWithGoogleRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthLoginRequest;
import com.ilhamrhmtkbr.domain.repository.AuthRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel implements ValidationFrontend<UserAuthLoginRequest> {
    private final AuthRepository repository;
    private final SingleLiveEvent<FormState<String>> loginState = new SingleLiveEvent<>();
    private final MutableLiveData<Map<String, String>> validationErrorFrontend = new MutableLiveData<>();
    private final MemberRepository memberRepository;
    private final SingleLiveEvent<FormState<UserResponse>> userData = new SingleLiveEvent<>();

    @Inject
    public LoginViewModel(AuthRepository repository, MemberRepository memberRepository) {
        this.repository = repository;
        this.memberRepository = memberRepository;
    }

    public SingleLiveEvent<FormState<String>> getLoginState() {
        return loginState;
    }

    public void login(UserAuthLoginRequest request) {
        repository.login(request, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                loginState.postValue(state);
            }
        });
    }

    public void loginWithGoogle(UserAuthLoginWithGoogleRequest request) {
        repository.loginWithGoogle(request, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                loginState.postValue(state);
            }
        });
    }

    public LiveData<Map<String, String>> getValidationFrontend() {
        return validationErrorFrontend;
    }

    @Override
    public boolean isValidValue(UserAuthLoginRequest request) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation username = new BaseValidation().validation("username", request.username)
                .required().minMax(5, 20);
        BaseValidation password = new BaseValidation().validation("password", request.password)
                .required().passwordCompleted();

        if (username.hasError()) {
            errors.put(username.getFieldName(), username.getError());
        }

        if (password.hasError()) {
            errors.put(password.getFieldName(), password.getError());
        }

        validationErrorFrontend.setValue(errors);

        return errors.isEmpty();
    }

    public void initUserData() {
        memberRepository.getMe(new FormCallback<UserResponse>() {
            @Override
            public void onResult(FormState<UserResponse> state) {
                userData.postValue(state);
            }
        });
    }

    public SingleLiveEvent<FormState<UserResponse>> getUserData() {
        return userData;
    }
}