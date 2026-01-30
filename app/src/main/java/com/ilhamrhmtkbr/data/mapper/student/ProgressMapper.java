package com.ilhamrhmtkbr.data.mapper.student;

import com.ilhamrhmtkbr.data.local.entity.StudentProgressesEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentProgressesPaginationEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentProgressResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentProgressesResponse;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.common.Section;
import com.ilhamrhmtkbr.domain.model.student.Progress;
import com.ilhamrhmtkbr.domain.model.student.ProgressDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProgressMapper {
    public static List<StudentProgressesEntity> fromResponseToEntities(StudentProgressesResponse response) {
        List<StudentProgressesEntity> newFormat = new ArrayList<>();
        if (response != null  && response.data != null) {
            for (StudentProgressesResponse.ProgressesItem item : response.data) {
                StudentProgressesEntity progressesEntity = new StudentProgressesEntity();
                progressesEntity.id = item.instructor_course_id;
                if (item.instructor_course != null && item.instructor_course.sections != null) {
                    progressesEntity.title = item.instructor_course.title;
                    progressesEntity.completed_sections = String.valueOf(item.sections.size());
                    progressesEntity.total_sections = String.valueOf(item.instructor_course.sections.size());
                }
                newFormat.add(progressesEntity);
            }
        }

        return newFormat;
    }

    public static List<StudentProgressesPaginationEntity> fromResponseToPaginationEntities(StudentProgressesResponse response) {
        List<StudentProgressesPaginationEntity> newFormat = new ArrayList<>();
        if (response != null  && response.links != null) {
            for (Page item : response.links) {
                StudentProgressesPaginationEntity pageEntity = new StudentProgressesPaginationEntity();
                pageEntity.url = item.getUrl();
                pageEntity.label = item.getLabel();
                pageEntity.isActive = item.getActive() != null ? item.getActive() : false;
                newFormat.add(pageEntity);
            }
        }

        return newFormat;
    }

    public static List<Progress> fromEntitiesToList(List<StudentProgressesEntity> entities) {
        List<Progress> newFormat = new ArrayList<>();
        if (entities != null) {
            for (StudentProgressesEntity item : entities) {
                newFormat.add(new Progress(item.id, item.title, item.completed_sections, item.total_sections));
            }
        }
        return newFormat;
    }

    public static ProgressDetail fromResponseToProgressDetail(StudentProgressResponse response) {
        if (response != null  && response.instructor_course != null) {
            Course course = new Course();
            course.setId(response.instructor_course.id);
            course.setTitle(response.instructor_course.title);
            course.setDescription(response.instructor_course.description);
            course.setImage(response.instructor_course.image);
            course.setPrice(response.instructor_course.price);
            course.setLevel(response.instructor_course.level);
            course.setStatus(response.instructor_course.status);

            List<Section> sections = new ArrayList<>();
            for (StudentProgressResponse.InstructorCourse.Section item: response.instructor_course.sections) {
                Section section = new Section();
                section.setId(item.id);
                section.setInstructorCourseId(item.instructor_course_id);
                sections.add(section);
            }

            course.setSections(sections);

            return new ProgressDetail(
                    response.id,
                    response.instructor_course_id,
                    response.sections,
                    response.created_at,
                    course
            );
        } else {
            return new ProgressDetail(
                    "null",
                    "null",
                    new HashMap<>(),
                    "null",
                    new Course()
            );
        }
    }
}