package com.ilhamrhmtkbr.presentation.guest.courses;

public class CourseFilterParams {
    public String keyword;
    public String page;
    public String orderBy;
    public String level;
    public String status;

    public CourseFilterParams(String keyword, String page, String orderBy, String level, String status) {
        this.keyword = keyword;
        this.page = page;
        this.orderBy = orderBy;
        this.level = level;
        this.status = status;
    }
}
