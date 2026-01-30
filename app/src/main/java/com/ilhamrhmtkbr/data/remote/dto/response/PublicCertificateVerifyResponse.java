package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class PublicCertificateVerifyResponse {
    @SerializedName("certificate_at")
    public String certificate_at;

    @SerializedName("student")
    public Student student;

    @SerializedName("instructor_course")
    public InstructorCourse instructor_course;

    public static class InstructorCourse {
        @SerializedName("title")
        public String title;
    }

    public static class Student {
        @SerializedName("user")
        public User user;

        public static class User {
            @SerializedName("full_name")
            public String full_name;
        }
    }
}