package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class PublicCourseDetailResponse {
    @SerializedName("course")
    public Course course;

    @SerializedName("likes")
    public int likes;

    public static class Course {
        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;

        @SerializedName("description")
        public String description;

        @SerializedName("image")
        public String image;

        @SerializedName("price")
        public String price;

        @SerializedName("level")
        public String level;

        @SerializedName("status")
        public String status;

        @SerializedName("notes")
        public String notes;

        @SerializedName("requirements")
        public String requirements;

        @SerializedName("instructor")
        public Instructor instructor;
    }

    public static class Instructor {
        @SerializedName("user")
        public User user;
    }

    public static class User {
        @SerializedName("full_name")
        public String full_name;
    }

    @SerializedName("isLikes")
    public Boolean isLikes;
}