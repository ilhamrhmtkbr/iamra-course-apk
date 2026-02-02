package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class UserAuthLoginWithGoogleRequest {

    @SerializedName("email")
    public String email;

    @SerializedName("idToken")
    public String idToken;

    @SerializedName("captcha")
    public String captcha;
}