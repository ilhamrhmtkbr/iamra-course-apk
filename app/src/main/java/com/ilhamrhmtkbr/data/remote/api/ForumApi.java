package com.ilhamrhmtkbr.data.remote.api;

import com.ilhamrhmtkbr.data.remote.dto.request.ForumMessageRequest;
import com.ilhamrhmtkbr.data.remote.dto.response.ForumChatResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.ForumGroupResponse;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ForumApi {
    @GET("data")
    Call<BaseResponseApi<List<ForumGroupResponse>>> getForumGroups();

    @GET("data/show")
    Call<BaseResponseApi<List<ForumChatResponse>>> getForumMessages(
            @Query("course_id") String courseId,
            @Query("before") String before
    );

    @POST("data")
    Call<BaseResponseApi<Object>> sendMessage(@Body ForumMessageRequest request);

}
