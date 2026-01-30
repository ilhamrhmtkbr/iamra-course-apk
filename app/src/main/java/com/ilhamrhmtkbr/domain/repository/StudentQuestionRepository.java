package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCourseQuestionUpSertRequest;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Question;

import java.util.List;

public interface StudentQuestionRepository {
    void fetch(String page, String sort);
    LiveData<Resource<List<Question>>> getAllQuestions();
    LiveData<List<Page>> getPaginationData();
    void show(String questionId);
    LiveData<Resource<Question>> getStudentQuestionDetailResult();
    void store(StudentCourseQuestionUpSertRequest request, FormCallback<String> callback);
    void modify(StudentCourseQuestionUpSertRequest request, FormCallback<String> callback);
    void delete(String questionId, FormCallback<String> callback);
}
