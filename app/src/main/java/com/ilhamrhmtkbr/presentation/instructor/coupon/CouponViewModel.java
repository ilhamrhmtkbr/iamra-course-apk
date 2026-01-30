package com.ilhamrhmtkbr.presentation.instructor.coupon;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorCouponUpSertRequest;
import com.ilhamrhmtkbr.domain.model.instructor.Coupon;
import com.ilhamrhmtkbr.domain.repository.InstructorCouponsRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CouponViewModel extends ViewModel implements ValidationFrontend<InstructorCouponUpSertRequest> {
    private final InstructorCouponsRepository couponsRepository;
    private final MutableLiveData<Map<String, String>> validationErrorsFrontend = new MutableLiveData<>();
    private final SingleLiveEvent<FormState<String>> couponFormState = new SingleLiveEvent<>();

    @Inject
    public CouponViewModel(InstructorCouponsRepository couponsRepository) {
        this.couponsRepository = couponsRepository;
    }

    public void show(String couponId) {
        couponsRepository.show(couponId);
    }

    public LiveData<Resource<Coupon>> getCouponResult() {
        return couponsRepository.getCouponResult();
    }

    public void store(String discount, String courseId, String expiryDate, String maxRedemptions) {
        InstructorCouponUpSertRequest request = new InstructorCouponUpSertRequest();
        request.instructorCourseId = courseId;
        request.discount = discount;
        request.expiryDate = expiryDate;
        request.maxRedemptions = maxRedemptions;

        if (isValidValue(request)){
            couponsRepository.store(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    couponFormState.postValue(state);
                }
            });
        }
    }

    public void modify(String couponId, String discount, String expiryDate, String maxRedemptions) {
        InstructorCouponUpSertRequest request = new InstructorCouponUpSertRequest();
        request.id = couponId;
        request.discount = discount;
        request.expiryDate = expiryDate;
        request.maxRedemptions = maxRedemptions;

        if (isValidValue(request)){
            couponsRepository.modify(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    couponFormState.postValue(state);
                }
            });
        }

    }

    public void delete(String couponId) {
        couponsRepository.delete(couponId, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                couponFormState.postValue(state);
            }
        });
    }

    public SingleLiveEvent<FormState<String>> getCouponFormState() {
        return couponFormState;
    }

    public InstructorCouponsRepository getCouponsRepository() {
        return couponsRepository;
    }

    @Override
    public boolean isValidValue(InstructorCouponUpSertRequest request) {
        Map<String, String> errors = new HashMap<>();
        BaseValidation discount = new BaseValidation().validation("discount", request.discount).required();
        BaseValidation expiryDate = new BaseValidation().validation("expiry_date", request.expiryDate).required();
        BaseValidation maxRedemptions = new BaseValidation().validation("max_redemptions", request.maxRedemptions).required();

        if (discount.hasError()) {
            errors.put(discount.getFieldName(), discount.getError());
        }
        if (expiryDate.hasError()) {
            errors.put(expiryDate.getFieldName(), expiryDate.getError());
        }
        if (maxRedemptions.hasError()) {
            errors.put(maxRedemptions.getFieldName(), maxRedemptions.getError());
        }

        validationErrorsFrontend.setValue(errors);

        return errors.isEmpty();
    }

    public MutableLiveData<Map<String, String>> getValidationErrorsFrontend() {
        return validationErrorsFrontend;
    }
}
