package com.ilhamrhmtkbr.domain.model.instructor;

public class Earning {
    private final String order_id;
    private final String instructorCourse;
    private final String studentFullName;
    private final String amount;
    private final String status;
    private final String createdAt;

    public Earning(String order_id, String instructorCourse, String studentFullName, String amount, String status, String createdAt) {
        this.order_id = order_id;
        this.instructorCourse = instructorCourse;
        this.studentFullName = studentFullName;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getInstructorCourse() {
        return instructorCourse;
    }

    public String getStudentFullName() {
        return studentFullName;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}