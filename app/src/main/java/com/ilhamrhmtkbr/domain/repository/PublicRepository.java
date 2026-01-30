package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.MediatorLiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCertificateVerifyResponse;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.common.Section;

import java.util.List;

public interface PublicRepository {
    void courses(String keyword, String page, String orderBy, String level, String status);
    MediatorLiveData<Resource<List<Course>>> getCoursesResult();
    MediatorLiveData<Resource<List<Page>>> getCoursesPagination();
    void course(String courseId);
    MediatorLiveData<Resource<Course>> getCourseResult();
    void courseSection(String courseId);
    MediatorLiveData<Resource<List<Section>>> getSectionResult();
    void certificateVerify(String certId, FormCallback<PublicCertificateVerifyResponse> callback);
}
