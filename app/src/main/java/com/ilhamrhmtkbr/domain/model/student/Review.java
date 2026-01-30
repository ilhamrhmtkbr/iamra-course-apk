package com.ilhamrhmtkbr.domain.model.student;

import com.ilhamrhmtkbr.domain.model.common.Course;

public class Review {
    private final String id;
    private final String review;
    private final String rating;
    private final String created_at;
    private final Course instructorCourse;

    public Review(String id, String review, String rating, String created_at, Course instructorCourse) {
        this.id = id;
        this.review = review;
        this.rating = rating;
        this.created_at = created_at;
        this.instructorCourse = instructorCourse;
    }

    public String getId() {
        return id;
    }

    public String getReview() {
        return review;
    }

    public String getRating() {
        return rating;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Course getInstructorCourse() {
        return instructorCourse;
    }
}