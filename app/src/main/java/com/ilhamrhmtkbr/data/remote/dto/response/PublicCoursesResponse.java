package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PublicCoursesResponse {
    @SerializedName("data")
    public List<Course> data;

    @SerializedName("meta")
    public Meta meta;

    public static class Course {
        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;

        @SerializedName("image")
        public String image;

        @SerializedName("instructor")
        public String instructor;

        @SerializedName("price")
        public int price;

        @SerializedName("editor")
        public String editor;
    }

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
}