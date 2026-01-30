package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class MemberUpdateAuthenticationRequest {
    @SerializedName("username")
    public String username;

    @SerializedName("old_password")
    public String oldPassword;

    @SerializedName("new_password")
    public String newPassword;

    public MemberUpdateAuthenticationRequest(String username, String oldPassword, String newPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
