package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class StudentTransactionStoreRequest {

    @SerializedName("instructor_course_id")
    public String instructorCourseId;

    @SerializedName("instructor_course_coupon")
    public String instructorCourseCoupon;
}