package com.ilhamrhmtkbr.data.remote.api;

import com.ilhamrhmtkbr.data.remote.dto.request.StudentCartStoreRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCertificateStoreRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCourseProgressStoreRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCourseQuestionUpSertRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCourseReviewUpSertRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentTransactionCheckCouponStoreRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentTransactionStoreRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentCartResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentCertificateResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentCertificatesResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentCoursesResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentLessonsResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentProgressResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentProgressesResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentQuestionResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentQuestionsResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentReviewsResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentSectionsResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentTransactionCheckCouponResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentTransactionDetailResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentTransactionResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.Query;
import retrofit2.http.Body;
import retrofit2.http.Streaming;

public interface StudentApi {

    @GET("carts")
    Call<BaseResponseApi<StudentCartResponse>> cartsFetch(@Query("page") String page, @Query("sort") String sort);

    @POST("carts")
    Call<BaseResponseApi<Void>> cartStore(@Body StudentCartStoreRequest requestStudentCartStore);

    @DELETE("carts")
    Call<BaseResponseApi<Void>> cartDelete(@Query("id") String id);

    @GET("certificates")
    Call<BaseResponseApi<StudentCertificatesResponse>> certificatesFetch(@Query("page") String page, @Query("sort") String sort);

    @GET("certificates/show")
    Call<StudentCertificateResponse> certificateFetch(@Query("id") String id);

    @POST("certificates")
    Call<BaseResponseApi<Void>> certificateStore(@Body StudentCertificateStoreRequest requestStudentCertificateStore);

    @GET("certificates/download")
    @Streaming
    Call<ResponseBody> certificateDownload(@Query("id") String id);

    @GET("certificates/link")
    Call<BaseResponseApi<String>> certificateLink(
            @Query("certificate_id") String certificateId,
            @Query("token") String token
    );

    @GET("progresses")
    Call<BaseResponseApi<StudentProgressesResponse>> courseProgressesFetch(@Query("page") String page);

    @GET("progresses/show")
    Call<BaseResponseApi<StudentProgressResponse>> courseProgressFetch(@Query("instructor_course_id") String courseId);

    @POST("progresses")
    Call<BaseResponseApi<Void>> courseProgressStore(@Body StudentCourseProgressStoreRequest requestStudentCourseProgressStore);

    @GET("reviews")
    Call<BaseResponseApi<StudentReviewsResponse>> courseReviewsFetch(@Query("page") String page, @Query("sort") String sort);

    @POST("reviews")
    Call<BaseResponseApi<Void>> courseReviewStore(@Body StudentCourseReviewUpSertRequest requestStudentCourseReviewUpSert);

    @PATCH("reviews")
    Call<BaseResponseApi<Void>> courseReviewModify(@Body StudentCourseReviewUpSertRequest requestStudentCourseReviewUpSert);

    @DELETE("reviews")
    Call<BaseResponseApi<Void>> courseReviewDelete(@Query("id") String id);

    @GET("studies/courses")
    Call<BaseResponseApi<StudentCoursesResponse>> coursesFetch(@Query("page") String page, @Query("sort") String sort);

    @GET("studies/sections")
    Call<BaseResponseApi<List<StudentSectionsResponse>>> sectionsFetch(@Query("course_id") String courseId);

    @GET("studies/lessons")
    Call<BaseResponseApi<StudentLessonsResponse>> lessonsFetch(@Query("section_id") String sectionId, @Query("page") String page);

    @GET("questions")
    Call<BaseResponseApi<StudentQuestionsResponse>> questionsFetch(@Query("page") String page, @Query("sort") String sort);

    @GET("questions/show")
    Call<BaseResponseApi<List<StudentQuestionResponse>>> questionFetch(@Query("id") String id);

    @POST("questions")
    Call<BaseResponseApi<Void>> questionStore(@Body StudentCourseQuestionUpSertRequest requestStudentCourseQuestionUpSert);

    @PATCH("questions")
    Call<BaseResponseApi<Void>> questionModify(@Body StudentCourseQuestionUpSertRequest requestStudentCourseQuestionUpSert);

    @DELETE("questions")
    Call<BaseResponseApi<Void>> questionDelete(@Query("id") String questionId);

    @GET("transactions")
    Call<BaseResponseApi<StudentTransactionResponse>> transactionsFetch(
            @Query("page") String page,
            @Query("sort") String sort,
            @Query("status") String status
    );

    @GET("transactions/show")
    Call<BaseResponseApi<StudentTransactionDetailResponse>> transactionFetch(@Query("order_id") String orderId);

    @POST("transactions")
    Call<BaseResponseApi<Void>> transactionStore(@Body StudentTransactionStoreRequest requestStudentTransactionStore);

    @POST("transactions/check-coupon")
    Call<BaseResponseApi<StudentTransactionCheckCouponResponse>> transactionCheckCoupon(@Body StudentTransactionCheckCouponStoreRequest requestStudentTransactionCheckCouponStore);

    @DELETE("transactions")
    Call<BaseResponseApi<Void>> transactionDelete(@Query("order_id") String orderId);
}
