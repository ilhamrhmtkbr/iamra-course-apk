package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

public class InstructorAccountResponse {

    @SerializedName("account_id")
    public String account_id;

    @SerializedName("bank_name")
    public String bank_name;

    @SerializedName("alias_name")
    public String alias_name;
}