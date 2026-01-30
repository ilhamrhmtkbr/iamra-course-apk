package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class ForumMessageRequest {

    @SerializedName("message")
    public String message;

    @SerializedName("course_id")
    public String courseId;

    public ForumMessageRequest(String message, String courseId) {
        this.message = message;
        this.courseId = courseId;
    }
}