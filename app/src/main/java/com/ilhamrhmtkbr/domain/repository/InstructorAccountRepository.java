package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorAccountUpSertRequest;
import com.ilhamrhmtkbr.domain.model.instructor.Account;

public interface InstructorAccountRepository {
    void fetch();
    LiveData<Resource<Account>> getAccount();
    void store(InstructorAccountUpSertRequest request, FormCallback<String> callback);
    void modify(InstructorAccountUpSertRequest request, FormCallback<String> callback);
}
