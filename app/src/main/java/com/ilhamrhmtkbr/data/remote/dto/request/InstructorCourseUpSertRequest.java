package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class InstructorCourseUpSertRequest {

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

    @SerializedName("visibility")
    public String visibility;

    @SerializedName("notes")
    public String notes;

    @SerializedName("editor")
    public String editor;

    @SerializedName("requirements")
    public String requirements;
}