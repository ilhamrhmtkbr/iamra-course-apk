package com.ilhamrhmtkbr.data.remote.api;

import com.ilhamrhmtkbr.data.remote.dto.request.InstructorAccountUpSertRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorAnswerUpSertRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorCouponUpSertRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorCourseUpSertRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorLessonUpSertRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorSectionUpSertRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorSocialUpSertRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorAccountResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorAnswersResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCouponResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCouponsResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCourseResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCoursesLikesResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCoursesResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorEarningsResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorLessonsResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorReviewsResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorSectionsResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorSocialsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface InstructorApi {
    @GET("account")
    Call<BaseResponseApi<InstructorAccountResponse>> accountFetch();

    @POST("account")
    Call<BaseResponseApi<String>> accountStore(@Body InstructorAccountUpSertRequest instructorAccountUpSertRequest);

    @PATCH("account")
    Call<BaseResponseApi<String>> accountModify(@Body InstructorAccountUpSertRequest instructorAccountUpSertRequest);

    @GET("answers")
    Call<InstructorAnswersResponse> answersFetch(@Query("page") String page, @Query("sort") String sort);

    @POST("answers")
    Call<BaseResponseApi<Void>> answerStore(@Body InstructorAnswerUpSertRequest requestInstructorAnswerUpSert);

    @PATCH("answers")
    Call<BaseResponseApi<Void>> answerModify(@Body InstructorAnswerUpSertRequest requestInstructorAnswerUpSert);

    @GET("coupons")
    Call<InstructorCouponsResponse> couponsFetch(@Query("page") String page, @Query("sort") String sort);

    @GET("coupons/show")
    Call<InstructorCouponResponse> couponFetch(@Query("id") String id);

    @POST("coupons")
    Call<BaseResponseApi<Void>> couponStore(@Body InstructorCouponUpSertRequest instructorCouponUpSertRequest);

    @PATCH("coupons")
    Call<BaseResponseApi<Void>> couponModify(@Body InstructorCouponUpSertRequest instructorCouponUpSertRequest);

    @DELETE("coupons")
    Call<BaseResponseApi<Void>> couponDelete(@Query("id") String id);

    @GET("courses")
    Call<InstructorCoursesResponse> coursesFetch(@Query("page") String page, @Query("sort") String sort);

    @GET("courses/show")
    Call<BaseResponseApi<InstructorCourseResponse>> courseFetch(@Query("id") String id);

    @POST("courses")
    Call<BaseResponseApi<Void>> courseStore(@Body InstructorCourseUpSertRequest requestInstructorCourseUpsert);

    @PATCH("courses")
    Call<BaseResponseApi<Void>> courseModify(@Body InstructorCourseUpSertRequest requestInstructorCourseUpsert);

    @DELETE("courses")
    Call<BaseResponseApi<Void>> courseDelete(@Query("id") String id);

    @GET("courses/likes")
    Call<BaseResponseApi<List<InstructorCoursesLikesResponse>>> courseLikesFetch();

    @GET("courses/reviews")
    Call<InstructorReviewsResponse> courseReviewsFetch(@Query("page") String page, @Query("sort") String sort);

    @GET("earnings")
    Call<InstructorEarningsResponse> earningsFetch(@Query("page") String page, @Query("sort") String sort);

    @POST("earnings")
    Call<BaseResponseApi<Void>> earningPayout();

    @GET("lessons")
    Call<BaseResponseApi<InstructorLessonsResponse>> lessonsFetch(@Query("page") String page, @Query("section_id") String sectionId);

    @POST("lessons")
    Call<BaseResponseApi<Void>> lessonStore(@Body InstructorLessonUpSertRequest requestInstructorLessonUpSert);

    @PATCH("lessons")
    Call<BaseResponseApi<Void>> lessonModify(@Body InstructorLessonUpSertRequest requestInstructorLessonUpSert);

    @DELETE("lessons")
    Call<BaseResponseApi<Void>> lessonDelete(@Query("instructor_section_id") String sectionId, @Query("id") String lessonId);

    @GET("sections")
    Call<BaseResponseApi<InstructorSectionsResponse>> sectionsFetch(@Query("course_id") String courseId);

    @POST("sections")
    Call<BaseResponseApi<Void>> sectionStore(@Body InstructorSectionUpSertRequest requestInstructorSectionUpSert);

    @PATCH("sections")
    Call<BaseResponseApi<Void>> sectionModify(@Body InstructorSectionUpSertRequest requestInstructorSectionUpSert);

    @DELETE("sections")
    Call<BaseResponseApi<Void>> sectionDelete(@Query("id") String sectionId);

    @GET("socials")
    Call<InstructorSocialsResponse> socialsFetch();

    @POST("socials")
    Call<BaseResponseApi<Void>> socialStore(@Body InstructorSocialUpSertRequest requestInstructorSocialUpSert);

    @PATCH("socials")
    Call<BaseResponseApi<Void>> socialModify(@Body InstructorSocialUpSertRequest requestInstructorSocialUpSert);

    @DELETE("socials")
    Call<BaseResponseApi<Void>> socialDelete(@Query("id") String socialId);
}
