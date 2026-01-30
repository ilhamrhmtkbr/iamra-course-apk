package com.ilhamrhmtkbr.presentation.auth.register;


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
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthRegisterRequest;
import com.ilhamrhmtkbr.domain.repository.AuthRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RegisterViewModel extends ViewModel implements ValidationFrontend<UserAuthRegisterRequest> {
    private final AuthRepository repository;
    private final SingleLiveEvent<FormState<String>> registerState = new SingleLiveEvent<>();
    private final MutableLiveData<Map<String, String>> validationFrontend = new MutableLiveData<>();
    private final MemberRepository memberRepository;
    private final SingleLiveEvent<FormState<UserResponse>> userData = new SingleLiveEvent<>();

    @Inject
    public RegisterViewModel(AuthRepository repository, MemberRepository memberRepository) {
        this.repository = repository;
        this.memberRepository = memberRepository;
    }

    public void register(UserAuthRegisterRequest request) {
        if (isValidValue(request)) {
            repository.register(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    registerState.postValue(state);
                }
            });
        }
    }

    public void loginWithGoogle(UserAuthLoginWithGoogleRequest request) {
        repository.loginWithGoogle(request, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                registerState.postValue(state);
            }
        });
    }

    public SingleLiveEvent<FormState<String>> getRegisterState() {
        return registerState;
    }

    public LiveData<Map<String, String>> getValidationFrontend() {
        return validationFrontend;
    }

    @Override
    public boolean isValidValue(UserAuthRegisterRequest request) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation firstName = new BaseValidation().validation("firstName", request.firstName)
                .required().minMax(5, 20);
        BaseValidation middleName = new BaseValidation().validation("middleName", request.middleName)
                .required().minMax(5, 20);
        BaseValidation lastName = new BaseValidation().validation("lastName", request.lastName)
                .required().minMax(5, 20);
        BaseValidation username = new BaseValidation().validation("username", request.username)
                .required().minMax(5, 20);
        BaseValidation password = new BaseValidation().validation("password", request.password)
                .required().passwordCompleted();
        BaseValidation passwordConfirm = new BaseValidation().validation("passwordConfirm", request.passwordConfirmation)
                .required().passwordCompleted().passwordConfirm(request.password);

        if (firstName.hasError()) {
            errors.put(firstName.getFieldName(), firstName.getError());
        }

        if (middleName.hasError()) {
            errors.put(middleName.getFieldName(), middleName.getError());
        }

        if (lastName.hasError()) {
            errors.put(lastName.getFieldName(), lastName.getError());
        }

        if (username.hasError()) {
            errors.put(username.getFieldName(), username.getError());
        }

        if (password.hasError()) {
            errors.put(password.getFieldName(), password.getError());
        }

        if (passwordConfirm.hasError()) {
            errors.put(passwordConfirm.getFieldName(), passwordConfirm.getError());
        }

        validationFrontend.setValue(errors);

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