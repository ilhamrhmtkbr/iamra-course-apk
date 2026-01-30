package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class UserAuthLoginRequest {
    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;

    public UserAuthLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
