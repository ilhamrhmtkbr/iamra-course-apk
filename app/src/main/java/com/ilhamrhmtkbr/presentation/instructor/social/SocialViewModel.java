package com.ilhamrhmtkbr.presentation.instructor.social;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorSocialUpSertRequest;
import com.ilhamrhmtkbr.domain.repository.InstructorSocialsRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SocialViewModel extends ViewModel implements ValidationFrontend<InstructorSocialUpSertRequest> {
    private final InstructorSocialsRepository socialsRepository;
    private final SingleLiveEvent<FormState<String>> socialsFormState = new SingleLiveEvent<>();
    private final MutableLiveData<Map<String, String>> validationErrorsFrontend = new MutableLiveData<>();

    @Inject
    public SocialViewModel(InstructorSocialsRepository socialsRepository) {
        this.socialsRepository = socialsRepository;
    }

    public void store(String displayName, String urlLink) {
        InstructorSocialUpSertRequest request = new InstructorSocialUpSertRequest();
        request.displayName = displayName;
        request.urlLink = urlLink;
        if (isValidValue(request)) {
            socialsRepository.store(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    socialsFormState.postValue(state);
                }
            });
        }
    }

    public void modify(String id, String displayName, String urlLink) {
        InstructorSocialUpSertRequest request = new InstructorSocialUpSertRequest();
        request.id = id;
        request.displayName = displayName;
        request.urlLink = urlLink;
        if (isValidValue(request)) {
            socialsRepository.modify(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    socialsFormState.postValue(state);
                }
            });
        }
    }

    public void delete(String socialId) {
        socialsRepository.delete(socialId, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                socialsFormState.postValue(state);
            }
        });
    }

    public LiveData<Map<String, String>> getValidationErrorsFrontend() {
        return validationErrorsFrontend;
    }

    public SingleLiveEvent<FormState<String>> getSocialsFormState() {
        return socialsFormState;
    }

    public InstructorSocialsRepository getSocialsRepository() {
        return socialsRepository;
    }

    @Override
    public boolean isValidValue(InstructorSocialUpSertRequest request) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation displayName = new BaseValidation().validation("display_name", request.displayName).required();
        BaseValidation urlLink = new BaseValidation().validation("url_link", request.urlLink).required();

        if (displayName.hasError()) {
            errors.put(displayName.getFieldName(), displayName.getError());
        }

        if (urlLink.hasError()) {
            errors.put(urlLink.getFieldName(), urlLink.getError());
        }

        validationErrorsFrontend.setValue(errors);

        return errors.isEmpty();
    }
}