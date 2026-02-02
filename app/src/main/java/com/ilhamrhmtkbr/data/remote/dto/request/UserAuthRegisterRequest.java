package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class UserAuthRegisterRequest {
    @SerializedName("first_name")
    public String firstName;

    @SerializedName("middle_name")
    public String middleName;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("username")
    public String username;

    @SerializedName("password")
    public String password;

    @SerializedName("password_confirmation")
    public String passwordConfirmation;

    @SerializedName("captcha")
    public String captcha;

    public UserAuthRegisterRequest(String firstName, String middleName, String lastName, String username, String password, String passwordConfirmation, String captcha) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.captcha = captcha;
    }
}
