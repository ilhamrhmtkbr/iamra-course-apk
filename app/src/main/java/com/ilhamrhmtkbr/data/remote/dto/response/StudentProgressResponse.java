package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class StudentProgressResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("instructor_course_id")
    public String instructor_course_id;

    @SerializedName("sections")
    public Map<String, String> sections;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("instructor_course")
    public InstructorCourse instructor_course;

    public static class InstructorCourse {
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

        @SerializedName("sections")
        public List<Section> sections;

        public static class Section {
            @SerializedName("id")
            public String id;

            @SerializedName("instructor_course_id")
            public String instructor_course_id;
        }
    }
}