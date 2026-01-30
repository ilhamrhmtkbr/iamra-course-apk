package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorAnswerUpSertRequest;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Answer;

import java.util.List;

public interface InstructorAnswersRepository {
    void fetch(String page, String sort);
    LiveData<Resource<List<Answer>>> getAnswers();
    LiveData<List<Page>> getAnswersPagination();
    void store(InstructorAnswerUpSertRequest request, FormCallback<String> callback);
    void modify(InstructorAnswerUpSertRequest request, FormCallback<String> callback);

}
