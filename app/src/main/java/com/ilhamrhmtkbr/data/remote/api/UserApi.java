package com.ilhamrhmtkbr.data.remote.api;

import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthLoginRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthLoginWithGoogleRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.UserAuthRegisterRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberRegisterAsStudentRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateAdditionalInfoRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateAuthenticationRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateEmailRequest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserApi {
    @POST("auth/login")
    Call<BaseResponseApi<String>> userLogin(@Body UserAuthLoginRequest request);
    @POST("auth/login-with-google")
    Call<BaseResponseApi<String>> userLoginWithGoggle(@Body UserAuthLoginWithGoogleRequest request);
    @POST("auth/register")
    Call<BaseResponseApi<String>> userRegister(@Body UserAuthRegisterRequest request);
    @GET("auth/logout")
    Call<BaseResponseApi<String>> userLogout();
    @GET("auth/me")
    Call<BaseResponseApi<UserResponse>> userGetData();
    @GET("auth/refresh")
    Call<BaseResponseApi<String>> refreshToken();

    @POST("member/student")
    Call<BaseResponseApi<String>> registerAsStudent(@Body MemberRegisterAsStudentRequest request);

    @Multipart
    @POST("member/instructor")
    Call<BaseResponseApi<String>> registerAsInstructor(
            @Part("role") RequestBody role,
            @Part("summary") RequestBody summary,
            @Part MultipartBody.Part resume
    );

    @POST("member/email")
    Call<BaseResponseApi<String>> memberUpdateEmail(@Body MemberUpdateEmailRequest request);

    @PATCH("member/authentication")
    Call<BaseResponseApi<String>> memberUpdateAuthentication(@Body MemberUpdateAuthenticationRequest request);

    @PATCH("member/additional-info")
    Call<BaseResponseApi<String>> memberUpdateAdditionalInfo(@Body MemberUpdateAdditionalInfoRequest request);

    @PATCH("member/course-like")
    Call<BaseResponseApi<Void>> memberUpSertCourseLike(@Query("id") String id);
}
