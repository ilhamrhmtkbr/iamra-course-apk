package com.ilhamrhmtkbr.presentation.student.transactions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Transaction;
import com.ilhamrhmtkbr.domain.repository.StudentTransactionRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TransactionsViewModel extends ViewModel {
    private final StudentTransactionRepository transactionRepository;
    private final MutableLiveData<String> sort = new MutableLiveData<>();
    private final MutableLiveData<String> status = new MutableLiveData<>();

    @Inject
    public TransactionsViewModel(StudentTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public LiveData<Resource<List<Transaction>>> getTransactions() {
        return transactionRepository.getAllTransactions();
    }

    public void refreshTransactions(String page, String sort, String status) {
        transactionRepository.fetch(page, sort, status);
    }

    public LiveData<List<Page>> getPaginationData() {
        return transactionRepository.getPaginationData();
    }

    public MutableLiveData<String> getSort() {
        return sort;
    }

    public MutableLiveData<String> getStatus() {
        return status;
    }
}
