package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstructorEarningsResponse {
    @SerializedName("data")
    public List<EarningItem> data;

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

    public static class EarningItem {
        @SerializedName("order_id")
        public String order_id;

        @SerializedName("instructor_course")
        public InstructorCourse instructor_course;

        @SerializedName("student_full_name")
        public String student_full_name;

        @SerializedName("amount")
        public String amount;

        @SerializedName("status")
        public String status;

        @SerializedName("created_at")
        public String created_at;
    }

    public static class InstructorCourse {
        @SerializedName("title")
        public String title;
    }
}