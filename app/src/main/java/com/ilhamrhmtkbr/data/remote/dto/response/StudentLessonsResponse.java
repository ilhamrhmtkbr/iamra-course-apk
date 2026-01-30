package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.List;

public class StudentLessonsResponse {
    @SerializedName("data")
    public List<LessonItem> data;

    @SerializedName("links")
    public List<Page> links;

    public static class LessonItem {
        @SerializedName("id")
        public String id;

        @SerializedName("instructor_section_id")
        public String instructor_section_id;

        @SerializedName("title")
        public String title;

        @SerializedName("description")
        public String description;

        @SerializedName("code")
        public String code;

        @SerializedName("order_in_section")
        public String order_in_section;
    }
}