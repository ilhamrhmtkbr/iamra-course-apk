package com.ilhamrhmtkbr.presentation.guest.courses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.repository.PublicRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CoursesViewModel extends ViewModel {
    private final MutableLiveData<CourseFilterParams> filterParams = new MutableLiveData<>();
    private final PublicRepository publicRepository;

    @Inject
    public CoursesViewModel(PublicRepository publicRepository) {
        this.publicRepository = publicRepository;
    }

    public void fetch() {
        CourseFilterParams params = filterParams.getValue();
        if (params != null) {
            publicRepository.courses(
                    params.keyword,
                    params.page,
                    params.orderBy,
                    params.level,
                    params.status
            );
        }
    }

    public LiveData<Resource<List<Course>>> getCoursesResult() {
        return publicRepository.getCoursesResult();
    }

    public LiveData<Resource<List<Page>>> getCoursesPagination() {
        return publicRepository.getCoursesPagination();
    }

    public void setFilterParams(CourseFilterParams params) {
        filterParams.setValue(params);
    }

    public LiveData<CourseFilterParams> getCurrentParams() {
        return filterParams;
    }
}
