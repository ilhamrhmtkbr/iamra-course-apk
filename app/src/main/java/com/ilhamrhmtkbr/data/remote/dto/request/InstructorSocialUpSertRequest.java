package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class InstructorSocialUpSertRequest {

    @SerializedName("id")
    public String id;

    @SerializedName("url_link")
    public String urlLink;

    @SerializedName("display_name")
    public String displayName;
}