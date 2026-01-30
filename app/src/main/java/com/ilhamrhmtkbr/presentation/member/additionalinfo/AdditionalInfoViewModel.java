package com.ilhamrhmtkbr.presentation.member.additionalinfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateAdditionalInfoRequest;
import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;
import com.ilhamrhmtkbr.domain.repository.MemberRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AdditionalInfoViewModel extends ViewModel implements ValidationFrontend<MemberUpdateAdditionalInfoRequest> {
    private final MemberRepository memberRepository;
    private final SingleLiveEvent<FormState<String>> additionalInfoFormState = new SingleLiveEvent<>();
    private final MutableLiveData<Map<String, String>> validationErrorsFrontend = new MutableLiveData<>();

    private String currentImage = "";
    private boolean hasNewImage = false;

    @Inject
    public AdditionalInfoViewModel(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void request(MemberUpdateAdditionalInfoRequest request) {
        if (isValidValue(request)) {
            memberRepository.updateAdditionalInfo(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    additionalInfoFormState.setValue(state);
                }
            });
        }
    }

    // ✅ Reset image dan flag
    public void resetImage() {
        this.currentImage = "";
        this.hasNewImage = false;
    }

    public String getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(String currentImage) {
        this.currentImage = currentImage;
    }

    public boolean hasNewImage() {
        return hasNewImage;
    }

    // ✅ TAMBAHKAN setter untuk hasNewImage
    public void setHasNewImage(boolean hasNewImage) {
        this.hasNewImage = hasNewImage;
    }

    @Override
    public boolean isValidValue(MemberUpdateAdditionalInfoRequest request) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation firstName = new BaseValidation().validation("first_name", request.firstName).required().minMax(1, 25);
        BaseValidation middleName = new BaseValidation().validation("middle_name", request.middleName).required().minMax(1, 25);
        BaseValidation lastName = new BaseValidation().validation("last_name", request.lastName).required().minMax(1, 25);
        BaseValidation image = new BaseValidation().validation("image", request.image).required();
        BaseValidation dob = new BaseValidation().validation("dob", request.dob).required().dob();
        BaseValidation address = new BaseValidation().validation("address", request.address).required();

        if (firstName.hasError()) {
            errors.put(firstName.getFieldName(), firstName.getError());
        }

        if (middleName.hasError()) {
            errors.put(middleName.getFieldName(), middleName.getError());
        }

        if (lastName.hasError()) {
            errors.put(lastName.getFieldName(), lastName.getError());
        }

        if (address.hasError()) {
            errors.put(address.getFieldName(), address.getError());
        }

        if (dob.hasError()) {
            errors.put(dob.getFieldName(), dob.getError());
        }

        if (image.hasError()) {
            errors.put(image.getFieldName(), image.getError());
        }

        validationErrorsFrontend.setValue(errors);

        return errors.isEmpty();
    }

    public LiveData<Map<String, String>> getValidationErrorsFrontend() {
        return validationErrorsFrontend;
    }

    public SingleLiveEvent<FormState<String>> getAdditionalInfoFormState() {
        return additionalInfoFormState;
    }

    public void refreshUserData() {
        memberRepository.getMe(new FormCallback<UserResponse>() {
            @Override
            public void onResult(FormState<UserResponse> state) {

            }
        });
    }
}