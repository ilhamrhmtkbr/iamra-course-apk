package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class StudentCourseReviewUpSertRequest {

    @SerializedName("instructor_course_id")
    public String instructorCourseId;

    @SerializedName("review")
    public String review;

    @SerializedName("rating")
    public String rating;
}