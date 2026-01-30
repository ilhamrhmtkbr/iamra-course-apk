package com.ilhamrhmtkbr.presentation.student.transactiondetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentTransactionDetailResponse;
import com.ilhamrhmtkbr.domain.repository.StudentStudiesRepository;
import com.ilhamrhmtkbr.domain.repository.StudentTransactionRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TransactionDetailViewModel extends ViewModel {
    private final StudentTransactionRepository transactionRepository;
    private final StudentStudiesRepository studiesRepository;
    private final MutableLiveData<FormState<String>> transactionFormState = new MutableLiveData<>();

    @Inject
    public TransactionDetailViewModel(StudentTransactionRepository transactionRepository, StudentStudiesRepository studiesRepository) {
        this.transactionRepository = transactionRepository;
        this.studiesRepository = studiesRepository;
    }

    public void requestDetail(String orderId) {
        transactionRepository.show(orderId);
    }

    public void requestDelete(String orderId) {
        transactionRepository.delete(orderId, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                transactionFormState.postValue(state);
            }
        });
    }

    public LiveData<Resource<StudentTransactionDetailResponse>> getTransactionsDetail(){
        return transactionRepository.getTransactionDetailResult();
    }

    public MutableLiveData<FormState<String>> getTransactionFormState() {
        return transactionFormState;
    }

    public void refreshTransaction() {
        transactionRepository.fetch("1", "desc", "all");
    }

    public void refreshCourses() {
        studiesRepository.courses("1", "desc");
    }
}
