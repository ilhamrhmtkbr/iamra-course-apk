package com.ilhamrhmtkbr.presentation.student.progresses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Progress;
import com.ilhamrhmtkbr.domain.repository.StudentProgressesRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProgressesViewModel extends ViewModel {
    private final StudentProgressesRepository progressesRepository;

    @Inject
    public ProgressesViewModel(StudentProgressesRepository progressesRepository) {
        this.progressesRepository = progressesRepository;
    }

    public LiveData<Resource<List<Progress>>> getProgresses() {
        return progressesRepository.getAllProgresses();
    }

    public void refreshProgresses(String page) {
        progressesRepository.fetch(page);
    }

    public LiveData<List<Page>> getPaginationData() {
        return progressesRepository.getAllPagination();
    }
}
