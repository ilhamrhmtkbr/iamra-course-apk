package com.ilhamrhmtkbr.domain.model.common;

public class Lesson {
    private String id;
    private String instructorSectionId;
    private String title;
    private String description;
    private String code;
    private String createdAt;
    private String orderInSection;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstructorSectionId() {
        return instructorSectionId;
    }

    public void setInstructorSectionId(String instructorSectionId) {
        this.instructorSectionId = instructorSectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrderInSection() {
        return orderInSection;
    }

    public void setOrderInSection(String orderInSection) {
        this.orderInSection = orderInSection;
    }
}