package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.List;

public class StudentReviewsResponse {
    @SerializedName("data")
    public List<ReviewItem> data;

    @SerializedName("links")
    public List<Page> links;

    public static class ReviewItem {
        @SerializedName("id")
        public String id;

        @SerializedName("review")
        public String review;

        @SerializedName("rating")
        public String rating;

        @SerializedName("created_at")
        public String created_at;

        @SerializedName("instructor_course")
        public InstructorCourse instructor_course;
    }

    public static class InstructorCourse {
        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;

        @SerializedName("description")
        public String description;
    }
}