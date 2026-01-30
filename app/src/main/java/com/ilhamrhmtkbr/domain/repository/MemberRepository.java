package com.ilhamrhmtkbr.domain.repository;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberRegisterAsStudentRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateAdditionalInfoRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateAuthenticationRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.MemberUpdateEmailRequest;
import com.ilhamrhmtkbr.data.remote.dto.response.UserResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public interface MemberRepository {
    void logout();
    void getMe(FormCallback<UserResponse> callback);
    void updateAdditionalInfo(MemberUpdateAdditionalInfoRequest request, FormCallback<String> callback);
    void updateAuthentication(MemberUpdateAuthenticationRequest request, FormCallback<String> callback);
    void updateEmail(MemberUpdateEmailRequest request, FormCallback<String> callback);
    void registerAsStudent(MemberRegisterAsStudentRequest request, FormCallback<String> callback);
    void registerAsInstructor(RequestBody role, RequestBody summary, MultipartBody.Part resume, FormCallback<String> callback);
    void storeLike(String courseId, FormCallback<String> callback);
}
