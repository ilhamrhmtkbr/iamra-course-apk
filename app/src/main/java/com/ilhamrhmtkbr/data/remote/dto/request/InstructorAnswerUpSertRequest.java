package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class InstructorAnswerUpSertRequest {

    @SerializedName("id")
    public String id;

    @SerializedName("student_question_id")
    public String studentQuestionId;

    @SerializedName("answer")
    public String answer;
}