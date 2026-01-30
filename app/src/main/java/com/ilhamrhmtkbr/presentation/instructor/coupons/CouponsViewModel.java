package com.ilhamrhmtkbr.presentation.instructor.coupons;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Coupon;
import com.ilhamrhmtkbr.domain.repository.InstructorCouponsRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CouponsViewModel extends ViewModel {
    private final InstructorCouponsRepository couponsRepository;
    private final MutableLiveData<String> sort = new MutableLiveData<>();

    @Inject
    public CouponsViewModel(InstructorCouponsRepository couponsRepository) {
        this.couponsRepository = couponsRepository;
    }

    public void refreshCoupons(String page, String sort){
        couponsRepository.fetch(page, sort);
    }

    public LiveData<Resource<List<Coupon>>> getCoupons() {
        return couponsRepository.getCoupons();
    }

    public LiveData<List<Page>> getPaginationData() {
        return couponsRepository.getCouponsPagination();
    }

    public MutableLiveData<String> getSort() {
        return sort;
    }
}
