package com.ilhamrhmtkbr.presentation.member.email;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateEmailRequest;
import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;
import com.ilhamrhmtkbr.domain.repository.MemberRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EmailViewModel extends ViewModel implements ValidationFrontend<MemberUpdateEmailRequest> {
    private final MemberRepository memberRepository;
    private final SingleLiveEvent<FormState<String>> emailFormState = new SingleLiveEvent<>();
    private final MutableLiveData<Map<String, String>> validationErrorsFrontend = new MutableLiveData<>();

    @Inject
    public EmailViewModel(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void request(MemberUpdateEmailRequest request) {
        if (isValidValue(request)) {
            memberRepository.updateEmail(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    emailFormState.postValue(state);
                }
            });
        }
    }

    @Override
    public boolean isValidValue(MemberUpdateEmailRequest request) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation email = new BaseValidation().validation("email", request.email).required();

        if (email.hasError()) {
            errors.put(email.getFieldName(), email.getError());
        }

        validationErrorsFrontend.setValue(errors);

        return errors.isEmpty();
    }

    public LiveData<FormState<String>> getEmailFormState() {
        return emailFormState;
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
