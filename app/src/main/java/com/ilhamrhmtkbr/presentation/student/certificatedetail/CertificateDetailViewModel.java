package com.ilhamrhmtkbr.presentation.student.certificatedetail;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.student.CertificateDetail;
import com.ilhamrhmtkbr.domain.repository.StudentCertificateRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CertificateDetailViewModel extends ViewModel {
    private final StudentCertificateRepository certificateRepository;
    private final SingleLiveEvent<FormState<String>> downloadResult = new SingleLiveEvent<>();

    @Inject
    public CertificateDetailViewModel(StudentCertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public void request(String certificateId) {
        certificateRepository.show(certificateId);
    }

    public void download(String certificateId, Context context) {
        certificateRepository.download(certificateId, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                downloadResult.postValue(state);
            }
        }, context.getApplicationContext());
    }

    public LiveData<Resource<CertificateDetail>> getCertificateDetail() {
        return certificateRepository.getCertificateDetailResult();
    }

    public SingleLiveEvent<FormState<String>> getDownloadResult() {
        return downloadResult;
    }
}
