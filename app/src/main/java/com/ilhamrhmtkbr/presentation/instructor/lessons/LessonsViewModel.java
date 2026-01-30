package com.ilhamrhmtkbr.presentation.instructor.lessons;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Lesson;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.repository.InstructorLessonsRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LessonsViewModel extends ViewModel {
    private final InstructorLessonsRepository lessonsRepository;

    @Inject
    public LessonsViewModel(InstructorLessonsRepository lessonsRepository) {
        this.lessonsRepository = lessonsRepository;
    }

    public void refresh(String page, String sectionId){
        lessonsRepository.fetch(page, sectionId);
    }

    public LiveData<Resource<List<Lesson>>> getLessons(String sectionId) {
        return lessonsRepository.getLessons(sectionId);
    }

    public LiveData<List<Page>> getPaginationData() {
        return lessonsRepository.getLessonsPagination();
    }
}