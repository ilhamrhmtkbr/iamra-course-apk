package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstructorCouponsResponse {
    @SerializedName("data")
    public List<CouponItem> data;

    @SerializedName("meta")
    public Meta meta;

    public static class Meta {
        @SerializedName("links")
        public List<Page> links;
    }

    public static class Page {
        @SerializedName("url")
        public String url;

        @SerializedName("label")
        public String label;

        @SerializedName("active")
        public Boolean active;
    }

    public static class CouponItem {
        @SerializedName("id")
        public String id;

        @SerializedName("discount")
        public String discount;

        @SerializedName("max_redemptions")
        public String max_redemptions;

        @SerializedName("expiry_date")
        public String expiry_date;
    }
}