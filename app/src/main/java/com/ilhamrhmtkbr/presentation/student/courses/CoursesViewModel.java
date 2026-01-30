package com.ilhamrhmtkbr.presentation.student.courses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.repository.StudentStudiesRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CoursesViewModel extends ViewModel {
    private final StudentStudiesRepository studiesRepository;
    private final MutableLiveData<String> sort = new MutableLiveData<>();

    @Inject
    public CoursesViewModel(StudentStudiesRepository studiesRepository) {
        this.studiesRepository = studiesRepository;
    }

    public LiveData<Resource<List<Course>>> getCourses() {
        return studiesRepository.getAllCourses();
    }


    public LiveData<List<Page>> getPaginationData() {
        return studiesRepository.getPaginationData();
    }

    public void refreshCourses(String page, String sort) {
        studiesRepository.courses(page, sort);
    }

    public MutableLiveData<String> getSort() {
        return sort;
    }
}
