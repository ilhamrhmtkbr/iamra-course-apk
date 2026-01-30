package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.List;

public class StudentCoursesResponse {
    @SerializedName("data")
    public List<CourseItem> data;

    @SerializedName("links")
    public List<Page> links;

    public static class CourseItem {
        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;

        @SerializedName("description")
        public String description;

        @SerializedName("image")
        public String image;

        @SerializedName("created_at")
        public String created_at;
    }
}