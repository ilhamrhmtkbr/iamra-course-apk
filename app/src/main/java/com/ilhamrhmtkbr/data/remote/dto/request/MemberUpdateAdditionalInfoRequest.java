package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class MemberUpdateAdditionalInfoRequest {
    @SerializedName("first_name")
    public String firstName;

    @SerializedName("middle_name")
    public String middleName;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("image")
    public String image;

    @SerializedName("address")
    public String address;

    @SerializedName("dob")
    public String dob;

    public MemberUpdateAdditionalInfoRequest(String firstName, String middleName, String lastName, String image, String address, String dob) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.image = image;
        this.address = address;
        this.dob = dob;
    }
}
