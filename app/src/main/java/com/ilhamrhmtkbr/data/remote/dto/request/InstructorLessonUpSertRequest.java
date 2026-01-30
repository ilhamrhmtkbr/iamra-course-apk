package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class InstructorLessonUpSertRequest {

    @SerializedName("id")
    public String id;

    @SerializedName("instructor_section_id")
    public String instructorSectionId;

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("code")
    public String code;

    @SerializedName("order_in_section")
    public String orderInSection;
}