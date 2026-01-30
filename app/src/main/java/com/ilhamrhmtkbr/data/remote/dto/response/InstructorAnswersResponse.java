package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;
import com.ilhamrhmtkbr.domain.model.common.Page;

import java.util.List;

public class InstructorAnswersResponse {
    @SerializedName("data")
    public List<AnswerItem> data;

    @SerializedName("links")
    public List<Page> links;

    public static class AnswerItem {
        @SerializedName("title")
        public String title;

        @SerializedName("question_id")
        public String question_id;

        @SerializedName("question")
        public String question;

        @SerializedName("question_created_at")
        public String question_created_at;

        @SerializedName("answer_id")
        public String answer_id;

        @SerializedName("answer")
        public String answer;

        @SerializedName("student")
        public String student;
    }
}