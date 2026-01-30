package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentCertificateResponse {
    @SerializedName("data")
    public CertificateItem data;

    public static class CertificateItem {
        @SerializedName("id")
        public String id;

        @SerializedName("course")
        public InstructorCourse course;

        @SerializedName("created_at")
        public String created_at;

        @SerializedName("instructor")
        public String instructor;
    }

    public static class InstructorCourse {
        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;

        @SerializedName("description")
        public String description;

        @SerializedName("image")
        public String image;

        @SerializedName("price")
        public int price;

        @SerializedName("level")
        public String level;

        @SerializedName("sections")
        public List<InstructorSections> sections;
    }

    public static class InstructorSections {
        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;
    }
}