package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstructorCoursesResponse {
    @SerializedName("data")
    public List<CourseItem> data;

    @SerializedName("meta")
    public Meta meta;

    public static class Meta {
        @SerializedName("links")
        public List<Page> links;
    }

    public static class Page {
        @SerializedName("url")
        public String url;

        @SerializedName("label")
        public String label;

        @SerializedName("active")
        public Boolean active;
    }

    public static class CourseItem {
        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;

        @SerializedName("image")
        public String image;

        @SerializedName("description")
        public String description;

        @SerializedName("editor")
        public String editor;
    }
}