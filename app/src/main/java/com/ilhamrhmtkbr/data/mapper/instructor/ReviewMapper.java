package com.ilhamrhmtkbr.data.mapper.instructor;

import com.ilhamrhmtkbr.data.local.entity.InstructorReviewsEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorReviewsPaginationEntity;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorReviewsResponse;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewMapper {
    public static List<InstructorReviewsEntity> fromResponseToEntitiesList(InstructorReviewsResponse response) {
        List<InstructorReviewsEntity> reviews = new ArrayList<>();
        if (response != null && response.data != null) {
            for (InstructorReviewsResponse.ReviewItem item : response.data) {
                InstructorReviewsEntity review = new InstructorReviewsEntity();
                review.course_title = item.course_title;
                review.student_full_name = item.student_full_name;
                review.student_review = item.student_review;
                review.student_rating = item.student_rating;
                review.created_at = item.created_at;
                reviews.add(review);
            }
        }
        return reviews;
    }

    public static List<InstructorReviewsPaginationEntity> fromResponseToPaginationEntitiesList(InstructorReviewsResponse response) {
        List<InstructorReviewsPaginationEntity> newFormat = new ArrayList<>();
        if (response != null && response.meta != null && response.meta.links != null) {
            for (InstructorReviewsResponse.Page item : response.meta.links) {
                InstructorReviewsPaginationEntity pageEntity = new InstructorReviewsPaginationEntity();
                pageEntity.url = item.url;
                pageEntity.label = item.label;
                pageEntity.isActive = item.active != null ? item.active : false;
                newFormat.add(pageEntity);
            }
        }
        return newFormat;
    }

    public static List<Review> fromEntitiesToList(List<InstructorReviewsEntity> entities) {
        List<Review> newFormat = new ArrayList<>();
        if (entities != null) {
            for (InstructorReviewsEntity item : entities) {
                newFormat.add(new Review(
                        item.course_title,
                        item.student_full_name,
                        item.student_review,
                        item.student_rating,
                        item.created_at
                ));
            }
        }

        return newFormat;
    }
}