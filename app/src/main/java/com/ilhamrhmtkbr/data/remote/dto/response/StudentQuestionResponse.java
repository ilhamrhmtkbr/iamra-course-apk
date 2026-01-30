package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class StudentQuestionResponse {
    @SerializedName("id")
    public String id;

    @SerializedName("question")
    public String question;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("course_title")
    public String course_title;

    @SerializedName("answers_id")
    public String answers_id;

    @SerializedName("answer")
    public String answer;

    @SerializedName("answer_created_at")
    public String answer_created_at;
}