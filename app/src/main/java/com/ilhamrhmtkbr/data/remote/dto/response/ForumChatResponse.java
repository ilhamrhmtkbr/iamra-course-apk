package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class ForumChatResponse {

    @SerializedName("username")
    public String username;

    @SerializedName("name")
    public String name;

    @SerializedName("message")
    public String message;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("role")
    public String role;

    @SerializedName("course_id")
    public String course_id;

    @SerializedName("id")
    public String id;
}