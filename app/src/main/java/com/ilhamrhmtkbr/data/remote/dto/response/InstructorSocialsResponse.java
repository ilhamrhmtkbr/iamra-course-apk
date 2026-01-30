package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstructorSocialsResponse {
    @SerializedName("data")
    public List<SocialItem> data;

    public static class SocialItem {
        @SerializedName("id")
        public String id;

        @SerializedName("url_link")
        public String url_link;

        @SerializedName("app_name")
        public String app_name;

        @SerializedName("display_name")
        public String display_name;
    }
}