package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.forum.ChatMapper;
import com.ilhamrhmtkbr.data.mapper.forum.GroupMapper;
import com.ilhamrhmtkbr.data.remote.dto.request.ForumMessageRequest;
import com.ilhamrhmtkbr.data.remote.dto.response.ForumChatResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.ForumGroupResponse;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.api.ForumApi;
import com.ilhamrhmtkbr.domain.model.forum.Chat;
import com.ilhamrhmtkbr.domain.model.forum.Group;
import com.ilhamrhmtkbr.domain.repository.ForumRepository;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForumRepositoryImpl implements ForumRepository {
    private final ForumApi api;
    private final MediatorLiveData<Resource<List<Group>>> forumResult = new MediatorLiveData<>();
    private final MediatorLiveData<Resource<List<Chat>>> chatResult = new MediatorLiveData<>();

    @Inject
    public ForumRepositoryImpl(ForumApi api) {
        this.api = api;
    }

    @Override
    public void fetchGroups() {
        forumResult.postValue(Resource.loading());
        api.getForumGroups().enqueue(new Callback<BaseResponseApi<List<ForumGroupResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<List<ForumGroupResponse>>> call, Response<BaseResponseApi<List<ForumGroupResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Group> groupList = GroupMapper.fromResponseList(response.body().data);
                    forumResult.postValue(Resource.success(groupList));
                } else {
                    forumResult.postValue(Resource.error(response.errorBody().toString()));
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<List<ForumGroupResponse>>> call, Throwable t) {
                forumResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<List<Group>>> getGroups() {
        return forumResult;
    }

    @Override
    public void sendMessage(ForumMessageRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());

        api.sendMessage(request).enqueue(new Callback<BaseResponseApi<Object>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Object>> call, Response<BaseResponseApi<Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else {
                    try{
                        String errorBody = response.errorBody().string();
                        ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                        validationErrorMapper.handle(errorBody, callback);
                    } catch (Exception e) {
                        callback.onResult(FormState.error(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<Object>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    @Override
    public void loadMessages(String courseId, String beforeTime) {
        chatResult.setValue(Resource.loading());
        api.getForumMessages(courseId, beforeTime).enqueue(new Callback<BaseResponseApi<List<ForumChatResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<List<ForumChatResponse>>> call, Response<BaseResponseApi<List<ForumChatResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chatResult.setValue(Resource.success(ChatMapper.fromResponse(response.body().data)));
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<List<ForumChatResponse>>> call, Throwable t) {
                chatResult.setValue(Resource.error(t.getMessage()));
            }
        });
    }

    public MediatorLiveData<Resource<List<Chat>>> getChatResult() {
        return chatResult;
    }
}
