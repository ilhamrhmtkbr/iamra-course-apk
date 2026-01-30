package com.ilhamrhmtkbr.presentation.member.authentication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateAuthenticationRequest;
import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;
import com.ilhamrhmtkbr.domain.repository.MemberRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthenticationViewModel extends ViewModel implements ValidationFrontend<MemberUpdateAuthenticationRequest> {
    private final MemberRepository memberRepository;
    private final SingleLiveEvent<FormState<String>> authenticationFormState = new SingleLiveEvent<>();
    private final MutableLiveData<Map<String, String>> validationErrorsFrontend = new MutableLiveData<>();

    @Inject
    public AuthenticationViewModel(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void request(MemberUpdateAuthenticationRequest request) {
        if (isValidValue(request)) {
            memberRepository.updateAuthentication(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    authenticationFormState.setValue(state);
                }
            });
        }
    }

    @Override
    public boolean isValidValue(MemberUpdateAuthenticationRequest request) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation username = new BaseValidation().validation("username", request.username).required();
        BaseValidation oldPassword = new BaseValidation().validation("old_password", request.oldPassword).required();
        BaseValidation newPassword = new BaseValidation().validation("new_password", request.newPassword).required();

        if(username.hasError()) {
            errors.put(username.getFieldName(), username.getError());
        }

        if(oldPassword.hasError()) {
            errors.put(oldPassword.getFieldName(), oldPassword.getError());
        }

        if(newPassword.hasError()) {
            errors.put(newPassword.getFieldName(), newPassword.getError());
        }

        validationErrorsFrontend.setValue(errors);

        return errors.isEmpty();
    }

    public LiveData<FormState<String>> getAuthenticationFormState() {
        return authenticationFormState;
    }

    public LiveData<Map<String, String>> getValidationErrorsFrontend() {
        return validationErrorsFrontend;
    }

    public void refreshUserData() {
        memberRepository.getMe(new FormCallback<UserResponse>() {
            @Override
            public void onResult(FormState<UserResponse> state) {

            }
        });
    }
}
