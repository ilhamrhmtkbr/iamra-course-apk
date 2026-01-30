package com.ilhamrhmtkbr.presentation.student.lessons;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCourseProgressStoreRequest;
import com.ilhamrhmtkbr.domain.repository.StudentProgressesRepository;
import com.ilhamrhmtkbr.domain.repository.StudentStudiesRepository;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentLessonsResponse;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LessonsViewModel extends ViewModel {
    private final StudentStudiesRepository studiesRepository;
    private final StudentProgressesRepository progressesRepository;
    private final SingleLiveEvent<FormState<String>> finishLessonsResult = new SingleLiveEvent<>();

    @Inject
    public LessonsViewModel(StudentStudiesRepository studiesRepository, StudentProgressesRepository progressesRepository) {
        this.studiesRepository = studiesRepository;
        this.progressesRepository = progressesRepository;
    }

    public void request(String sectionId, String page) {
        studiesRepository.lessons(sectionId, page);
    }

    public LiveData<Resource<StudentLessonsResponse>> getLessonsResult() {
        return studiesRepository.getLessonsResult();
    };

    public void finish(String courseId, String sectionId, String sectionTitle) {
        StudentCourseProgressStoreRequest request = new StudentCourseProgressStoreRequest();
        request.instructorCourseId = courseId;
        request.instructorSectionId = sectionId;
        request.instructorSectionTitle = sectionTitle;
        progressesRepository.store(request, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                finishLessonsResult.postValue(state);
            }
        });
    }

    public SingleLiveEvent<FormState<String>> getFinishLessonsResult() {
        return finishLessonsResult;
    }
}
