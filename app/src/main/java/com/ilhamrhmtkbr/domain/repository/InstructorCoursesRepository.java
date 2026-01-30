package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorCourseUpSertRequest;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCourseResponse;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.CoursesLike;

import java.util.List;

public interface InstructorCoursesRepository {
    void fetch(String page, String sort);
    LiveData<Resource<List<Course>>> getCourses(String page, String sort);
    LiveData<List<Page>> getCoursesPagination();
    void show(String courseId);
    LiveData<Resource<InstructorCourseResponse>> getCourseResult();
    void store(InstructorCourseUpSertRequest request, FormCallback<String> callback);
    void modify(InstructorCourseUpSertRequest request, FormCallback<String> callback);
    void delete(String courseId, FormCallback<String> callback);
    void fetchCourseLikes();
    LiveData<Resource<List<CoursesLike>>> getCoursesLikes();
}

