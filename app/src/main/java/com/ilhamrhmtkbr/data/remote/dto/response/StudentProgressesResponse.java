package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.List;
import java.util.Map;

public class StudentProgressesResponse {
    @SerializedName("links")
    public List<Page> links;

    @SerializedName("data")
    public List<ProgressesItem> data;

    public static class ProgressesItem {
        @SerializedName("id")
        public String id;

        @SerializedName("instructor_course_id")
        public String instructor_course_id;

        @SerializedName("sections")
        public Map<String, String> sections;

        @SerializedName("instructor_course")
        public InstructorCourse instructor_course;

        public static class InstructorCourse {
            @SerializedName("title")
            public String title;

            @SerializedName("sections")
            public List<Sections> sections;

            public static class Sections {
                @SerializedName("id")
                public String id;
            }
        }
    }
}