package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class StudentTransactionCheckCouponResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("instructor_course_id")
    public String instructor_course_id;

    @SerializedName("discount")
    public String discount;

    @SerializedName("max_redemptions")
    public String max_redemptions;

    @SerializedName("expiry_date")
    public String expiry_date;
}