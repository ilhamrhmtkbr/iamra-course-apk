package com.ilhamrhmtkbr.data.mapper.student;

import com.ilhamrhmtkbr.data.local.entity.StudentQuestionsEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentQuestionsPaginationEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentQuestionResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentQuestionsResponse;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionMapper {
    public static List<StudentQuestionsEntity> fromResponseToEntities(StudentQuestionsResponse response) {
        List<StudentQuestionsEntity> newFormat = new ArrayList<>();
        if (response != null && response.data != null) {
            for (StudentQuestionsResponse.QuestionItem item : response.data) {
                StudentQuestionsEntity questionEntity = new StudentQuestionsEntity();
                questionEntity.id = item.id;
                questionEntity.instructor_course_id = item.instructor_course_id;
                questionEntity.question = item.question;
                questionEntity.created_at = item.created_at;
                questionEntity.course_title = item.instructor_course.title;
                newFormat.add(questionEntity);
            }
        }

        return newFormat;
    }

    public static List<StudentQuestionsPaginationEntity> fromResponseToPaginationEntities(StudentQuestionsResponse response) {
        List<StudentQuestionsPaginationEntity> newFormat = new ArrayList<>();
        if (response != null && response.links != null) {
            for (Page item : response.links) {
                StudentQuestionsPaginationEntity pageEntity = new StudentQuestionsPaginationEntity();
                pageEntity.url = item.getUrl();
                pageEntity.label = item.getLabel();
                pageEntity.isActive = item.getActive() != null ? item.getActive() : false;
                newFormat.add(pageEntity);
            }
        }

        return newFormat;
    }

    public static List<Question> fromEntitiesToList(List<StudentQuestionsEntity> entities) {
        List<Question> newFormat = new ArrayList<>();
        if (entities != null) {
            for (StudentQuestionsEntity item : entities) {
                newFormat.add(new Question(
                        item.id,
                        item.instructor_course_id,
                        item.question,
                        item.created_at,
                        item.course_title,
                        null,
                        null,
                        null
                ));
            }
        }

        return newFormat;
    }

    public static Question fromResponseToQuestionDetail(StudentQuestionResponse response) {
        if (response != null) {
            return new Question(
                    response.id,
                    null,
                    response.question,
                    response.created_at,
                    response.course_title,
                    response.answers_id,
                    response.answer,
                    response.answer_created_at
            );
        } else {
            return new Question(
                    "null",
                    null,
                    "null",
                    "null",
                    "null",
                    "null",
                    "null",
                    "null"
            );
        }
    }
}