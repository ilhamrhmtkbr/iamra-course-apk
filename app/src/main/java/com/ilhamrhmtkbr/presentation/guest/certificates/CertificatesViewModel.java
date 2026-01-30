package com.ilhamrhmtkbr.presentation.guest.certificates;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCertificateVerifyResponse;
import com.ilhamrhmtkbr.domain.repository.PublicRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CertificatesViewModel extends ViewModel implements ValidationFrontend<String> {
    private final PublicRepository repository;
    private final SingleLiveEvent<FormState<PublicCertificateVerifyResponse>> certificateState = new SingleLiveEvent<>();
    private final MutableLiveData<Map<String, String>> validationFrontend = new MutableLiveData<>();

    @Inject
    public CertificatesViewModel(PublicRepository repository) {
        this.repository = repository;
    }

    public void verifyCertificate(String certCode) {
        if (isValidValue(certCode)) {
            repository.certificateVerify(certCode, new FormCallback<PublicCertificateVerifyResponse>() {
                @Override
                public void onResult(FormState<PublicCertificateVerifyResponse> state) {
                    certificateState.postValue(state);
                }
            });
        }
    }

    public LiveData<FormState<PublicCertificateVerifyResponse>> getCertificateState() {
        return certificateState;
    }

    public LiveData<Map<String, String>> getValidationFrontend() {
        return validationFrontend;
    }

    @Override
    public boolean isValidValue(String inputValue) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation certificateId = new BaseValidation().validation("certificate_id", inputValue)
                .required();

        if (certificateId.hasError()) {
            errors.put(certificateId.getFieldName(), certificateId.getError());
        } else {
        }

        validationFrontend.setValue(errors);

        return errors.isEmpty();
    }
}
