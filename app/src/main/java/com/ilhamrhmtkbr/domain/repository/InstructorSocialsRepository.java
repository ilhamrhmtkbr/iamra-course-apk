package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorSocialUpSertRequest;
import com.ilhamrhmtkbr.domain.model.instructor.Social;

import java.util.List;

public interface InstructorSocialsRepository {
    void fetch();
    LiveData<Resource<List<Social>>> getSocials();
    void store(InstructorSocialUpSertRequest request, FormCallback<String> callback);
    void modify(InstructorSocialUpSertRequest request, FormCallback<String> callback);
    void delete(String id, FormCallback<String> callback);
}
