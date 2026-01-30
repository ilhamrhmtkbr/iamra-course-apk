package com.ilhamrhmtkbr.data.remote.dto.request;

import com.google.gson.annotations.SerializedName;

public class StudentCourseProgressStoreRequest {

    @SerializedName("instructor_course_id")
    public String instructorCourseId;

    @SerializedName("instructor_section_id")
    public String instructorSectionId;

    @SerializedName("instructor_section_title")
    public String instructorSectionTitle;
}