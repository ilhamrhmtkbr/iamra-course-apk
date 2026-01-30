package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class InstructorCouponResponse {
    @SerializedName("data")
    public CouponItem data;

    public static class CouponItem {
        @SerializedName("id")
        public String id;

        @SerializedName("code")
        public String code;

        @SerializedName("discount")
        public String discount;

        @SerializedName("max_redemptions")
        public String max_redemptions;

        @SerializedName("expiry_date")
        public String expiry_date;

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

        @SerializedName("status")
        public String status;
    }
}