package com.ilhamrhmtkbr.core.base;

import com.google.gson.annotations.SerializedName;

public class BaseResponseApi<T> {

    @SerializedName("success")
    public boolean success;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public T data;
}