package com.ilhamrhmtkbr.domain.model.instructor;

public class Coupon {
    private String id;
    private String code;
    private String discount;
    private String maxRedemptions;
    private String expiryDate;
    private String createdAt;
    private String instructorCourseTitle;
    private String instructorCourseStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getMaxRedemptions() {
        return maxRedemptions;
    }

    public void setMaxRedemptions(String maxRedemptions) {
        this.maxRedemptions = maxRedemptions;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getInstructorCourseTitle() {
        return instructorCourseTitle;
    }

    public void setInstructorCourseTitle(String instructorCourseTitle) {
        this.instructorCourseTitle = instructorCourseTitle;
    }

    public String getInstructorCourseStatus() {
        return instructorCourseStatus;
    }

    public void setInstructorCourseStatus(String instructorCourseStatus) {
        this.instructorCourseStatus = instructorCourseStatus;
    }
}