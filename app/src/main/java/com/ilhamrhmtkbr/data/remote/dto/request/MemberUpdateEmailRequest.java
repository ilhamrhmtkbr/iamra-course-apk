package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class MemberUpdateEmailRequest {
    @SerializedName("email")
    public String email;

    public MemberUpdateEmailRequest(String email) {
        this.email = email;
    }
}
