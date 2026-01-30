package com.ilhamrhmtkbr.domain.model.student;

import com.ilhamrhmtkbr.domain.model.common.Course;

import java.util.Map;

public class ProgressDetail {
    private final String id;
    private final String instructor_course_id;
    private final Map<String, String> sections;
    private final String created_at;
    private final Course instructor_course;

    public ProgressDetail(String id, String instructor_course_id, Map<String, String> sections, String created_at, Course instructor_course) {
        this.id = id;
        this.instructor_course_id = instructor_course_id;
        this.sections = sections;
        this.created_at = created_at;
        this.instructor_course = instructor_course;
    }

    public String getId() {
        return id;
    }

    public String getInstructor_course_id() {
        return instructor_course_id;
    }

    public Map<String, String> getSections() {
        return sections;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Course getInstructor_course() {
        return instructor_course;
    }
}