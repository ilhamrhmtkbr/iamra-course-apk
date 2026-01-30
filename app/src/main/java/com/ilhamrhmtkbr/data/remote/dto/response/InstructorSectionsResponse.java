package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstructorSectionsResponse {
    @SerializedName("data")
    public List<SectionItem> data;

    public static class SectionItem {
        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String title;

        @SerializedName("order_in_course")
        public String order_in_course;
    }
}