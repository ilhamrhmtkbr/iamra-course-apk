package com.ilhamrhmtkbr.data.mapper.student;

import com.ilhamrhmtkbr.data.local.entity.StudentCoursesEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentCoursesPaginationEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentCoursesResponse;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.ArrayList;
import java.util.List;

public class StudiesMapper {
    public static List<StudentCoursesEntity> fromResponseToEntities(StudentCoursesResponse response) {
        List<StudentCoursesEntity> newFormat = new ArrayList<>();
        if (response != null && response.data != null) {
            for (StudentCoursesResponse.CourseItem item : response.data) {
                StudentCoursesEntity courseEntity = new StudentCoursesEntity();
                courseEntity.id = item.id;
                courseEntity.title = item.title;
                courseEntity.description = item.description;
                courseEntity.image = item.image;
                courseEntity.created_at = item.created_at;
                newFormat.add(courseEntity);
            }
        }

        return newFormat;
    }

    public static List<StudentCoursesPaginationEntity> fromResponseToPaginationEntities(StudentCoursesResponse response) {
        List<StudentCoursesPaginationEntity> newFormat = new ArrayList<>();
        if (response != null && response.links != null) {
            for (Page item : response.links) {
                StudentCoursesPaginationEntity pageEntity = new StudentCoursesPaginationEntity();
                pageEntity.url = item.getUrl();
                pageEntity.label = item.getLabel();
                pageEntity.isActive = item.getActive() != null ? item.getActive() : false;
                newFormat.add(pageEntity);
            }
        }

        return newFormat;
    }

    public static List<Course> fromEntitiesToList(List<StudentCoursesEntity> entities) {
        List<Course> newFormat = new ArrayList<>();
        if (entities != null) {
            for (StudentCoursesEntity item : entities) {
                Course course = new Course();
                course.setId(item.id);
                course.setTitle(item.title);
                course.setDescription(item.description);
                course.setImage(item.image);
                course.setCreatedAt(item.created_at);
                newFormat.add(course);
            }
        }

        return newFormat;
    }
}
