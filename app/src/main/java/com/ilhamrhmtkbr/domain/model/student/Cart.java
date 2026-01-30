package com.ilhamrhmtkbr.domain.model.student;

import com.ilhamrhmtkbr.domain.model.common.Course;

public class Cart {
    private final String id;
    private final String instructorCourseId;
    private final Course instructorCourse;

    public Cart(String id, String instructorCourseId, Course instructorCourse) {
        this.id = id;
        this.instructorCourseId = instructorCourseId;
        this.instructorCourse = instructorCourse;
    }

    public String getId() {
        return id;
    }

    public String getInstructorCourseId() {
        return instructorCourseId;
    }

    public Course getInstructorCourse() {
        return instructorCourse;
    }
}