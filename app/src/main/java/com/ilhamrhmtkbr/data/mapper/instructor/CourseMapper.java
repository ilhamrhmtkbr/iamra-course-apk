package com.ilhamrhmtkbr.data.mapper.instructor;

import com.ilhamrhmtkbr.core.utils.ui.TextUtil;
import com.ilhamrhmtkbr.data.local.entity.InstructorCoursesEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorCoursesLikesEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorCoursesPaginationEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCourseResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCoursesLikesResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCoursesResponse;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.CoursesLike;

import java.util.ArrayList;
import java.util.List;

public class CourseMapper {
    public static List<InstructorCoursesEntity> fromResponseToEntities(InstructorCoursesResponse response) {
        List<InstructorCoursesEntity> newFormat = new ArrayList<>();
        if (response != null && response.data != null) {
            for (InstructorCoursesResponse.CourseItem item : response.data) {
                InstructorCoursesEntity coursesEntity = new InstructorCoursesEntity();
                coursesEntity.id = item.id;
                coursesEntity.title = item.title;
                coursesEntity.description = item.description;
                coursesEntity.image = item.image;
                coursesEntity.editor = item.editor;
                newFormat.add(coursesEntity);
            }
        }
        return newFormat;
    }

    public static List<InstructorCoursesPaginationEntity> fromResponseToPaginationEntities(InstructorCoursesResponse response) {
        List<InstructorCoursesPaginationEntity> newFormat = new ArrayList<>();
        if (response != null && response.meta != null && response.meta.links != null) {
            for (InstructorCoursesResponse.Page item : response.meta.links) {
                InstructorCoursesPaginationEntity pageEntity = new InstructorCoursesPaginationEntity();
                pageEntity.url = item.url;
                pageEntity.label = item.label;
                pageEntity.isActive = item.active != null ? item.active : false;
                newFormat.add(pageEntity);
            }
        }

        return newFormat;
    }

    public static List<Course> fromEntitiesToList(List<InstructorCoursesEntity> entities) {
        List<Course> newFormat = new ArrayList<>();
        if (entities != null) {
            for (InstructorCoursesEntity item : entities) {
                Course course = new Course();
                course.setId(item.id);
                course.setTitle(item.title);
                course.setDescription(item.description);
                course.setImage(item.image);
                course.setEditor(item.editor);
                newFormat.add(course);
            }
        }
        return newFormat;
    }

    public static List<InstructorCoursesLikesEntity> fromResponseCourseLikesToEntities(List<InstructorCoursesLikesResponse> response) {
        List<InstructorCoursesLikesEntity> newFormat = new ArrayList<>();
        if (response != null) {
            for (InstructorCoursesLikesResponse item : response) {
                InstructorCoursesLikesEntity like = new InstructorCoursesLikesEntity();
                like.username = item.username;
                like.full_name = item.full_name;
                newFormat.add(like);
            }
        }

        return newFormat;
    }

    public static List<CoursesLike> fromEntitiesToCourseLikeModel(List<InstructorCoursesLikesEntity> entities) {
        List<CoursesLike> newFormat = new ArrayList<>();
        if (entities != null) {
            for (InstructorCoursesLikesEntity item: entities) {
                newFormat.add(new CoursesLike(item.username, item.username));
            }
        }
        return newFormat;
    }
}