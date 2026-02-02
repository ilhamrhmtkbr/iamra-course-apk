package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class UserAuthLoginRequest {
    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;

    @SerializedName("captcha")
    public String captcha;

    public UserAuthLoginRequest(String username, String password, String captcha) {
        this.username = username;
        this.password = password;
        this.captcha = captcha;
    }
}
