package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.List;

public class StudentCartResponse {
    @SerializedName("data")
    public List<CartItem> data;

    @SerializedName("links")
    public List<Page> links;

    public static class CartItem {
        @SerializedName("id")
        public String id;

        @SerializedName("instructor_course_id")
        public String instructor_course_id;

        @SerializedName("instructor_course")
        public InstructorCourse instructor_course;

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
            public String price;

            @SerializedName("level")
            public String level;

            @SerializedName("status")
            public String status;
        }
    }
}