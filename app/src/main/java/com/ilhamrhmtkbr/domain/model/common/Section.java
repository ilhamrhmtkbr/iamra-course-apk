package com.ilhamrhmtkbr.domain.model.common;

public class Section {
    private String id;
    private String instructorCourseId;
    private String title;
    private String orderInCourse;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstructorCourseId() {
        return instructorCourseId;
    }

    public void setInstructorCourseId(String instructorCourseId) {
        this.instructorCourseId = instructorCourseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrderInCourse() {
        return orderInCourse;
    }

    public void setOrderInCourse(String orderInCourse) {
        this.orderInCourse = orderInCourse;
    }
}