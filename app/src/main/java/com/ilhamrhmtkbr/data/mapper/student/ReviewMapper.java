package com.ilhamrhmtkbr.data.mapper.student;

import com.ilhamrhmtkbr.data.local.entity.StudentReviewsEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentReviewsPaginationEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentReviewsResponse;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewMapper {
    public static List<StudentReviewsEntity> fromResponseToEntities(StudentReviewsResponse response) {
        List<StudentReviewsEntity> newFormat = new ArrayList<>();
        if (response != null && response.data != null) {
            for (StudentReviewsResponse.ReviewItem item : response.data) {
                StudentReviewsEntity reviewEntity = new StudentReviewsEntity();
                reviewEntity.id = item.id;
                reviewEntity.review = item.review;
                reviewEntity.rating = item.rating;
                if (item.instructor_course != null) {
                    reviewEntity.course_id = item.instructor_course.id;
                    reviewEntity.course_title = item.instructor_course.title;
                    reviewEntity.course_desc = item.instructor_course.description;
                }
                reviewEntity.created_at = item.created_at;
                newFormat.add(reviewEntity);
            }
        }

        return newFormat;
    }

    public static List<StudentReviewsPaginationEntity> fromResponseToPaginationEntities(StudentReviewsResponse response) {
        List<StudentReviewsPaginationEntity> newFormat = new ArrayList<>();
        if (response != null && response.links != null) {
            for (Page item : response.links) {
                StudentReviewsPaginationEntity pageEntity = new StudentReviewsPaginationEntity();
                pageEntity.url = item.getUrl();
                pageEntity.label = item.getLabel();
                pageEntity.isActive = item.getActive() != null ? item.getActive() : false;
                newFormat.add(pageEntity);
            }
        }

        return newFormat;
    }

    public static List<Review> fromEntitiesToList(List<StudentReviewsEntity> entities) {
        List<Review> newFormat = new ArrayList<>();
        if (entities != null) {
            for (StudentReviewsEntity item : entities) {
                Course course = new Course();
                course.setId(item.course_id);
                course.setTitle(item.course_title);
                course.setDescription(item.course_desc);
                newFormat.add(new Review(
                        item.id,
                        item.review,
                        item.rating,
                        item.created_at,
                        course
                ));
            }
        }

        return newFormat;
    }
}