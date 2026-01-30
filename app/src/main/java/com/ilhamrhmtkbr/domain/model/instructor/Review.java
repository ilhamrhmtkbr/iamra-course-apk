package com.ilhamrhmtkbr.domain.model.instructor;

public class Review {
    private final String courseTitle;
    private final String studentFullName;
    private final String studentReview;
    private final String studentRating;
    private final String createdAt;

    public Review(String courseTitle, String studentFullName, String studentReview, String studentRating, String createdAt) {
        this.courseTitle = courseTitle;
        this.studentFullName = studentFullName;
        this.studentReview = studentReview;
        this.studentRating = studentRating;
        this.createdAt = createdAt;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getStudentFullName() {
        return studentFullName;
    }

    public String getStudentReview() {
        return studentReview;
    }

    public String getStudentRating() {
        return studentRating;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}