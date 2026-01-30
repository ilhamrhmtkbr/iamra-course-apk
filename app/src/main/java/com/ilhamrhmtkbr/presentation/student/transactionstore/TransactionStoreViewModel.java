package com.ilhamrhmtkbr.presentation.student.transactionstore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentTransactionCheckCouponStoreRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentTransactionStoreRequest;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentTransactionCheckCouponResponse;
import com.ilhamrhmtkbr.domain.repository.StudentTransactionRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TransactionStoreViewModel extends ViewModel {
    private final StudentTransactionRepository transactionRepository;
    private final SingleLiveEvent<FormState<StudentTransactionCheckCouponResponse>> couponFormState = new SingleLiveEvent<>();
    private final SingleLiveEvent<FormState<String>> transactionFormState = new SingleLiveEvent<>();

    @Inject
    public TransactionStoreViewModel(StudentTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void checkCoupon(String courseId) {
        StudentTransactionCheckCouponStoreRequest request = new StudentTransactionCheckCouponStoreRequest();
        request.courseId = courseId;
        transactionRepository.checkCoupon(request, new FormCallback<StudentTransactionCheckCouponResponse>() {
            @Override
            public void onResult(FormState<StudentTransactionCheckCouponResponse> state) {
                couponFormState.postValue(state);
            }
        });
    }

    public LiveData<FormState<StudentTransactionCheckCouponResponse>> getCouponFormState() {
        return couponFormState;
    }

    public void storeTransaction(String courseId, String couponId) {
        StudentTransactionStoreRequest request = new StudentTransactionStoreRequest();
        request.instructorCourseId = courseId;
        request.instructorCourseCoupon = couponId;
        transactionRepository.store(request, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                transactionFormState.postValue(state);
            }
        });
    }

    public SingleLiveEvent<FormState<String>> getTransactionFormState() {
        return transactionFormState;
    }

    public void refreshTransaction(){
        transactionRepository.fetch("1", "desc", "all");
    }
}
