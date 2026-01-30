package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.ForumMessageRequest;
import com.ilhamrhmtkbr.domain.model.forum.Chat;
import com.ilhamrhmtkbr.domain.model.forum.Group;

import java.util.List;

public interface ForumRepository {
    void fetchGroups();
    LiveData<Resource<List<Group>>> getGroups();
    void sendMessage(ForumMessageRequest request, FormCallback<String> callback);
    void loadMessages(String courseId, String beforeTime);
    MediatorLiveData<Resource<List<Chat>>> getChatResult();

}
