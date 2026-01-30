package com.ilhamrhmtkbr.presentation.student.certificates;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Certificate;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.repository.StudentCertificateRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CertificatesViewModel extends ViewModel {
    private final StudentCertificateRepository certificateRepository;
    private final MutableLiveData<String> sort = new MutableLiveData<>("desc");

    @Inject
    public CertificatesViewModel(StudentCertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public LiveData<Resource<List<Certificate>>> getCertificates() {
        return certificateRepository.getAllCertificates();
    }

    public void refreshCertificates(String page, String sort) {
        certificateRepository.fetch(page, sort);
    }

    public LiveData<List<Page>> getPaginationData() {
        return certificateRepository.getPaginationData();
    }

    public MutableLiveData<String> getSort() {
        return sort;
    }

}
