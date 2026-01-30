package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.List;

public class StudentQuestionsResponse {
    @SerializedName("data")
    public List<QuestionItem> data;

    @SerializedName("links")
    public List<Page> links;

    public static class QuestionItem {
        @SerializedName("id")
        public String id;

        @SerializedName("instructor_course_id")
        public String instructor_course_id;

        @SerializedName("question")
        public String question;

        @SerializedName("created_at")
        public String created_at;

        @SerializedName("instructor_course")
        public InstructorCourse instructor_course;
    }

    public static class InstructorCourse {
        @SerializedName("title")
        public String title;
    }
}