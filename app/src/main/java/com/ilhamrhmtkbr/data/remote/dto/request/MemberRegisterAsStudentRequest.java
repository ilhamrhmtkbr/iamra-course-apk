package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class MemberRegisterAsStudentRequest {
    @SerializedName("role")
    public String role;

    @SerializedName("category")
    public String category;

    @SerializedName("summary")
    public String summary;

    public MemberRegisterAsStudentRequest(String role, String category, String summary) {
        this.role = role;
        this.category = category;
        this.summary = summary;
    }
}
