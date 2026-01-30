package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class InstructorCourseResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("instructor_id")
    public String instructor_id;

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

    @SerializedName("requirements")
    public String requirements;

    @SerializedName("editor")
    public String editor;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("updated_at")
    public String updated_at;
}