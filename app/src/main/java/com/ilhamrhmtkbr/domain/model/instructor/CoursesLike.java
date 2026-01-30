package com.ilhamrhmtkbr.domain.model.instructor;

public class CoursesLike {
    private String username;
    private String fullName;

    public CoursesLike(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }
}