package com.ilhamrhmtkbr.data.remote.api;

import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCertificateVerifyResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCourseDetailResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCourseSectionResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.PublicCoursesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PublicApi {
    @GET("courses")
    Call<PublicCoursesResponse> getCourses(
            @Query("keyword") String keyword,
            @Query("page") String page,
            @Query("order_by") String order_by,
            @Query("level") String level,
            @Query("status") String status
    );

    @GET("course")
    Call<BaseResponseApi<PublicCourseDetailResponse>> getCourse(
            @Query("id") String id
    );

    @GET("section")
    Call<BaseResponseApi<List<PublicCourseSectionResponse>>> getCourseSection(
            @Query("course_id") String course_id
    );

    @GET("student/certificate/verify")
    Call<BaseResponseApi<PublicCertificateVerifyResponse>> getCertificateVerify(
            @Query("id") String id
    );
}
