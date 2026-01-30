package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstructorReviewsResponse {
    @SerializedName("data")
    public List<ReviewItem> data;

    @SerializedName("meta")
    public Meta meta;

    public static class Meta {
        @SerializedName("links")
        public List<Page> links;
    }

    public static class Page {
        @SerializedName("url")
        public String url;

        @SerializedName("label")
        public String label;

        @SerializedName("active")
        public Boolean active;
    }

    public static class ReviewItem {
        @SerializedName("course_title")
        public String course_title;

        @SerializedName("student_full_name")
        public String student_full_name;

        @SerializedName("student_review")
        public String student_review;

        @SerializedName("student_rating")
        public String student_rating;

        @SerializedName("created_at")
        public String created_at;
    }
}