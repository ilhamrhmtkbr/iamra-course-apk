package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class PublicCourseSectionResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("order_in_course")
    public String order_in_course;
}