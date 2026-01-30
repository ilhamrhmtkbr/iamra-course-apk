package com.ilhamrhmtkbr.data.remote.dto.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * User model
 */
public class UserResponse {
    @SerializedName("username")
    private String username;

    @SerializedName("full_name")
    private String full_name;

    @SerializedName("image")
    private String image;

    @SerializedName("email")
    private String email;

    @SerializedName("email_verified_at")
    private String emailVerifiedAt;

    @SerializedName("role")
    private String role;

    @SerializedName("dob")
    private String dob;

    @SerializedName("address")
    private String address;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("summary")
    private String summary;

    @SerializedName("category")
    private String category;

    @SerializedName("resume")
    private String resume;

    // Constructors
    public UserResponse() {
    }

    // FIXED: Bug di constructor original
    public UserResponse(String username, String full_name, String email) {
        this.username = username;
        this.full_name = full_name; // Was: UserModel.this.full_name (wrong!)
        this.email = email;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(String emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    // Required for AuthManager serialization
    @Override
    public String toString() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this);
        } catch (Exception e) {
            return super.toString();
        }
    }

    // Required for AuthManager deserialization
    public static JSONObject fromString(String jsonString) {
        try {
            return new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Utility methods
    public boolean isEmailVerified() {
        return emailVerifiedAt != null && !emailVerifiedAt.isEmpty();
    }

    public boolean hasImage() {
        return image != null && !image.isEmpty();
    }
}