package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class InstructorAccountUpSertRequest {

    @SerializedName("account_id")
    public String accountId;

    @SerializedName("bank_name")
    public String bankName;

    @SerializedName("alias_name")
    public String aliasName;
}