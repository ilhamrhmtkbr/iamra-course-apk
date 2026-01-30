package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorLessonUpSertRequest;
import com.ilhamrhmtkbr.domain.model.common.Lesson;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.List;

public interface InstructorLessonsRepository {
    void fetch(String page, String sectionId);
    LiveData<Resource<List<Lesson>>> getLessons(String sectionId);
    LiveData<List<Page>> getLessonsPagination();
    void store(InstructorLessonUpSertRequest request, FormCallback<String> callback);
    void modify(InstructorLessonUpSertRequest request, FormCallback<String> callback);
    void delete(String sectionId, String lessonId, FormCallback<String> callback);
}

