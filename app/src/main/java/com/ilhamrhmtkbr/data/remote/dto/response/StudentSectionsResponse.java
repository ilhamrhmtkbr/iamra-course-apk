package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentSectionsResponse {
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

    @SerializedName("sections")
    public List<Sections> sections;

    public static class Sections {
        @SerializedName("id")
        public String id;

        @SerializedName("instructor_course_id")
        public String instructor_course_id;

        @SerializedName("title")
        public String title;
    }
}