package com.ilhamrhmtkbr.data.mapper.instructor;

import com.ilhamrhmtkbr.data.local.entity.InstructorAnswersEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorAnswersPaginationEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorAnswersResponse;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Answer;

import java.util.ArrayList;
import java.util.List;

public class AnswerMapper {
    public static List<InstructorAnswersEntity> fromResponseToEntities(InstructorAnswersResponse response) {
        List<InstructorAnswersEntity> newFormat = new ArrayList<>();
        if (response != null && response.data != null) {
            for (InstructorAnswersResponse.AnswerItem item : response.data) {
                InstructorAnswersEntity answerEntity = new InstructorAnswersEntity();
                answerEntity.course_title = item.title;
                answerEntity.question_id = item.question_id;
                answerEntity.question = item.question;
                answerEntity.question_created_at = item.question_created_at;
                answerEntity.answer = item.answer;
                answerEntity.answer_id = item.answer_id;
                answerEntity.student_name = item.student;
                newFormat.add(answerEntity);
            }
        }

        return newFormat;
    }

    public static List<InstructorAnswersPaginationEntity> fromResponseToPaginationEntities(InstructorAnswersResponse response) {
        List<InstructorAnswersPaginationEntity> newFormat = new ArrayList<>();
        if (response != null && response.links != null) {
            for (Page item : response.links) {
                InstructorAnswersPaginationEntity pageEntity = new InstructorAnswersPaginationEntity();
                pageEntity.url = item.getUrl();
                pageEntity.label = item.getLabel();
                pageEntity.isActive = item.getActive() != null ? item.getActive() : false;
                newFormat.add(pageEntity);
            }
        }

        return newFormat;
    }

    public static List<Answer> fromEntitiesToList(List<InstructorAnswersEntity> entities) {
        List<Answer> newFormat = new ArrayList<>();
        if (entities != null) {
            for (InstructorAnswersEntity item : entities) {
                Answer answer = new Answer();
                answer.setTitle(item.course_title);
                answer.setQuestionId(item.question_id);
                answer.setQuestion(item.question);
                answer.setQuestionCreatedAt(item.question_created_at);
                answer.setAnswerId(item.answer_id);
                answer.setAnswer(item.answer);
                answer.setStudent(item.student_name);
                newFormat.add(answer);
            }
        }
        return newFormat;
    }
}
