package com.ilhamrhmtkbr.presentation.student.sections;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentSectionsResponse;
import com.ilhamrhmtkbr.domain.model.student.ProgressDetail;
import com.ilhamrhmtkbr.domain.repository.StudentProgressesRepository;
import com.ilhamrhmtkbr.domain.repository.StudentStudiesRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SectionsViewModel extends ViewModel {
    private final StudentStudiesRepository studiesRepository;
    private final StudentProgressesRepository progressesRepository;

    @Inject
    public SectionsViewModel(StudentStudiesRepository studiesRepository, StudentProgressesRepository progressesRepository) {
        this.studiesRepository = studiesRepository;
        this.progressesRepository = progressesRepository;
    }

    public void request(String courseId) {
        studiesRepository.sections(courseId);
    }

    public LiveData<Resource<List<StudentSectionsResponse>>> getSections() {
        return studiesRepository.getSectionsResult();
    }

    public void fetchProgressDetail(String courseId) {
        progressesRepository.show(courseId);
    }

    public LiveData<Resource<ProgressDetail>> getProgressDetail() {
        return progressesRepository.getProgressDetailResult();
    }

}
