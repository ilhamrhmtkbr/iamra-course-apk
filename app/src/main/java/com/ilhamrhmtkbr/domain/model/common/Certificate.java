package com.ilhamrhmtkbr.domain.model.common;

public class Certificate {
    private String id;
    private String createdAt;
    private String studentName;
    private String instructorCourseTitle;
    private String instructorName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getInstructorCourseTitle() {
        return instructorCourseTitle;
    }

    public void setInstructorCourseTitle(String instructorCourseTitle) {
        this.instructorCourseTitle = instructorCourseTitle;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName){
        this.instructorName = instructorName;
    }
}
