package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.List;

public class StudentCertificatesResponse {
    @SerializedName("data")
    public List<CertificateItem> data;

    @SerializedName("links")
    public List<Page> links;

    public static class CertificateItem {
        @SerializedName("id")
        public String id;

        @SerializedName("instructor_course")
        public InstructorCourse instructor_course;

        @SerializedName("created_at")
        public String created_at;

        @SerializedName("student")
        public Student student;
    }

    public static class InstructorCourse {
        @SerializedName("title")
        public String title;
    }

    public static class Student {
        @SerializedName("user")
        public User user;
    }

    public static class User {
        @SerializedName("full_name")
        public String full_name;
    }
}