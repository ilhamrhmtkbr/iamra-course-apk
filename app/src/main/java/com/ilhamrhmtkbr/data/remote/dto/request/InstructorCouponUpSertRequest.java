package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class InstructorCouponUpSertRequest {

    @SerializedName("id")
    public String id;

    @SerializedName("instructor_course_id")
    public String instructorCourseId;

    @SerializedName("discount")
    public String discount;

    @SerializedName("max_redemptions")
    public String maxRedemptions;

    @SerializedName("expiry_date")
    public String expiryDate;
}