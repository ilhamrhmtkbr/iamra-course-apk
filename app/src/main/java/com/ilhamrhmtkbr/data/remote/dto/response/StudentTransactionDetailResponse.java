package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class StudentTransactionDetailResponse {
    @SerializedName("order_id")
    public String order_id;

    @SerializedName("instructor_course_coupon_id")
    public String instructor_course_coupon_id;

    @SerializedName("amount")
    public String amount;

    @SerializedName("midtrans_data")
    public String midtrans_data;

    @SerializedName("status")
    public String status;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("instructor_course")
    public InstructorCourse instructor_course;

    @SerializedName("instructor_course_coupon")
    public InstructorCourseCoupon instructor_course_coupon;

    public static class InstructorCourse {
        @SerializedName("title")
        public String title;
    }

    public static class InstructorCourseCoupon {
        @SerializedName("discount")
        public String discount;

        @SerializedName("expiry_date")
        public String expiry_date;
    }
}