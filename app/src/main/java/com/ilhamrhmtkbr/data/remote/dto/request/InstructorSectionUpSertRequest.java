package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class InstructorSectionUpSertRequest {

    @SerializedName("instructor_course_id")
    public String instructorCourseId;

    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("order_in_course")
    public String orderInCourse;
}