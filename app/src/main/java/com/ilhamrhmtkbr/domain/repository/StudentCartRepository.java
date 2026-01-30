package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCartStoreRequest;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Cart;

import java.util.List;

public interface StudentCartRepository {
    void fetch(String page, String sort);
    LiveData<List<Page>> getPaginationData();
    LiveData<Resource<List<Cart>>> getAllCarts();
    void store(StudentCartStoreRequest request, FormCallback<String> callback);
    void delete(String cartId, FormCallback<String> callback);
}
