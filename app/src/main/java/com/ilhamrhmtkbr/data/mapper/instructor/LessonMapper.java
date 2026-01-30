package com.ilhamrhmtkbr.data.mapper.instructor;

import com.ilhamrhmtkbr.data.local.entity.InstructorLessonsEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorLessonsPaginationEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorLessonsResponse;
import com.ilhamrhmtkbr.domain.model.common.Lesson;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.ArrayList;
import java.util.List;

public class LessonMapper {
    public static List<InstructorLessonsEntity> fromResponseToListEntities(InstructorLessonsResponse response) {
        List<InstructorLessonsEntity> newFormat = new ArrayList<>();
        if (response != null && response.data != null) {
            for (InstructorLessonsResponse.LessonItem item : response.data) {
                InstructorLessonsEntity lessonEntity = new InstructorLessonsEntity();
                lessonEntity.id = item.id;
                lessonEntity.section_id = item.instructor_section_id;
                lessonEntity.title = item.title;
                lessonEntity.description = item.description;
                lessonEntity.code = item.code;
                lessonEntity.created_at = item.created_at;
                lessonEntity.order_in_section = item.order_in_section;
                newFormat.add(lessonEntity);
            }
        }
        return newFormat;
    }

    public static List<InstructorLessonsPaginationEntity> fromResponseToPaginationListEntities(InstructorLessonsResponse response) {
        List<InstructorLessonsPaginationEntity> pages = new ArrayList<>();
        if (response != null && response.links != null) {
            for (Page item : response.links) {
                InstructorLessonsPaginationEntity pageEntity = new InstructorLessonsPaginationEntity();
                pageEntity.url = item.getUrl();
                pageEntity.label = item.getLabel();
                pageEntity.isActive = item.getActive() != null ? item.getActive() : false;
                pages.add(pageEntity);
            }
        }
        return pages;
    }

    public static List<Lesson> fromEntitiesToFromList(List<InstructorLessonsEntity> entities) {
        List<Lesson> newFormat = new ArrayList<>();
        if (entities != null) {
            for (InstructorLessonsEntity entity : entities) {
                Lesson lesson = new Lesson();
                lesson.setId(entity.id);
                lesson.setInstructorSectionId(entity.section_id);
                lesson.setTitle(entity.title);
                lesson.setDescription(entity.description);
                lesson.setCode(entity.code);
                lesson.setCreatedAt(entity.created_at);
                lesson.setOrderInSection(entity.order_in_section);
                newFormat.add(lesson);
            }
        }
        return newFormat;
    }
}
