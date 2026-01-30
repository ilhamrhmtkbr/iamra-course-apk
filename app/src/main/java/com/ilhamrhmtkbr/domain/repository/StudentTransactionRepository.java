package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentTransactionCheckCouponStoreRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentTransactionStoreRequest;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentTransactionCheckCouponResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentTransactionDetailResponse;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Transaction;

import java.util.List;

public interface StudentTransactionRepository {
    void fetch(String page, String sort, String status);
    LiveData<Resource<List<Transaction>>> getAllTransactions();
    LiveData<List<Page>> getPaginationData();
    void show(String orderId);
    LiveData<Resource<StudentTransactionDetailResponse>> getTransactionDetailResult();
    void checkCoupon(StudentTransactionCheckCouponStoreRequest request, FormCallback<StudentTransactionCheckCouponResponse> callback);
    void store(StudentTransactionStoreRequest request, FormCallback<String> callback);
    void delete(String orderId, FormCallback<String> callback);
}
