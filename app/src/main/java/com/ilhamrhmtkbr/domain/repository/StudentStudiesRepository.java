package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentLessonsResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentSectionsResponse;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.List;

public interface StudentStudiesRepository {
    void courses(String page, String sort);
    LiveData<List<Page>> getPaginationData();
    LiveData<Resource<List<Course>>> getAllCourses();
    void sections(String courseId);
    LiveData<Resource<List<StudentSectionsResponse>>> getSectionsResult();
    void lessons(String sectionId, String page);
    LiveData<Resource<StudentLessonsResponse>> getLessonsResult();
}
