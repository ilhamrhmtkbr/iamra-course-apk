package com.ilhamrhmtkbr.presentation.student.progress;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCertificateStoreRequest;
import com.ilhamrhmtkbr.domain.model.student.ProgressDetail;
import com.ilhamrhmtkbr.domain.repository.StudentCertificateRepository;
import com.ilhamrhmtkbr.domain.repository.StudentProgressesRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProgressViewModel extends ViewModel {
    private final StudentProgressesRepository progressesRepository;
    private final StudentCertificateRepository certificateRepository;
    private final SingleLiveEvent<FormState<String>> submitCompletedCourseResult = new SingleLiveEvent<>();

    @Inject
    public ProgressViewModel(StudentProgressesRepository progressesRepository, StudentCertificateRepository certificateRepository) {
        this.progressesRepository = progressesRepository;
        this.certificateRepository = certificateRepository;
    }

    public void request(String courseId) {
        progressesRepository.show(courseId);
    }

    public LiveData<Resource<ProgressDetail>> getProgressDetail() {
        return progressesRepository.getProgressDetailResult();
    }

    public void submitCompletedCourse(String courseId){
        StudentCertificateStoreRequest request = new StudentCertificateStoreRequest();
        request.instructorCourseId = courseId;

        certificateRepository.store(request, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                submitCompletedCourseResult.postValue(state);
            }
        });
    }

    public LiveData<FormState<String>> getSubmitCompletedCourseResult() {
        return submitCompletedCourseResult;
    }
}
