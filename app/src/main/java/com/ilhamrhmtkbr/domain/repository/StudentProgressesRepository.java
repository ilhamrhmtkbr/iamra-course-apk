package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCourseProgressStoreRequest;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Progress;
import com.ilhamrhmtkbr.domain.model.student.ProgressDetail;

import java.util.List;

public interface StudentProgressesRepository {
    void fetch(String page);
    LiveData<Resource<List<Progress>>> getAllProgresses();
    LiveData<List<Page>> getAllPagination();
    void show(String courseId);
    LiveData<Resource<ProgressDetail>> getProgressDetailResult();
    void store(StudentCourseProgressStoreRequest request, FormCallback<String> callback);
}
