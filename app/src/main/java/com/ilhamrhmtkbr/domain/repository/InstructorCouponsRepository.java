package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorCouponUpSertRequest;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Coupon;

import java.util.List;

public interface InstructorCouponsRepository {
    void fetch(String page, String sort);
    LiveData<Resource<List<Coupon>>> getCoupons();
    LiveData<List<Page>> getCouponsPagination();
    void show(String couponId);
    LiveData<Resource<Coupon>> getCouponResult();
    void store(InstructorCouponUpSertRequest request, FormCallback<String> callback);
    void modify(InstructorCouponUpSertRequest request, FormCallback<String> callback);
    void delete(String couponId, FormCallback<String> callback);
}
