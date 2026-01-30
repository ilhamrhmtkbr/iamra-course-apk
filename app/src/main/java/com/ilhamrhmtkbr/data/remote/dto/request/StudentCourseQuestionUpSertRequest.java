package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class StudentCourseQuestionUpSertRequest {

    @SerializedName("instructor_course_id")
    public String instructorCourseId;

    @SerializedName("id")
    public String id;

    @SerializedName("question")
    public String question;
}