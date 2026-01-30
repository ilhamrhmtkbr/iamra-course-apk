package com.ilhamrhmtkbr.domain.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCertificateStoreRequest;
import com.ilhamrhmtkbr.domain.model.common.Certificate;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.CertificateDetail;

import java.util.List;

public interface StudentCertificateRepository {
    void fetch(String page, String sort);
    LiveData<List<Page>> getPaginationData();
    LiveData<Resource<List<Certificate>>> getAllCertificates();
    void show(String certificateId);
    LiveData<Resource<CertificateDetail>> getCertificateDetailResult();
    void store(StudentCertificateStoreRequest request, FormCallback<String> callback);
    void download(String certificateId, FormCallback<String> callback, Context context);
}
