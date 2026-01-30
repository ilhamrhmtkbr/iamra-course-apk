package com.ilhamrhmtkbr.presentation.instructor.courses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.repository.InstructorCoursesRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CoursesViewModel extends ViewModel {
    private final MutableLiveData<String> sort = new MutableLiveData<>("desc");
    private final MutableLiveData<String> currentPage = new MutableLiveData<>("1");
    private final MutableLiveData<String> currentSort = new MutableLiveData<>("desc");
    private final InstructorCoursesRepository repository;
    private LiveData<Resource<List<Course>>> courses;

    @Inject
    public CoursesViewModel(InstructorCoursesRepository repository) {
        this.repository = repository;
    }

    public void refreshCourses(String page, String sort) {
        currentPage.setValue(page);
        currentSort.setValue(sort);
        repository.fetch(page, sort);
    }

    public LiveData<Resource<List<Course>>> getCourses() {
        return repository.getCourses(currentPage.getValue(), currentSort.getValue());
    }

    public LiveData<List<Page>> getPaginationData() {
        return repository.getCoursesPagination();
    }

    public MutableLiveData<String> getSort() {
        return sort;
    }
}