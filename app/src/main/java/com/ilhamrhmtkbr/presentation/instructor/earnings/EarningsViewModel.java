package com.ilhamrhmtkbr.presentation.instructor.earnings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Earning;
import com.ilhamrhmtkbr.domain.repository.InstructorEarningsRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class EarningsViewModel extends ViewModel {
    private final InstructorEarningsRepository earningsRepository;
    private final MutableLiveData<String> sort = new MutableLiveData<>();
    private final SingleLiveEvent<FormState<String>> payoutResult = new SingleLiveEvent<>();

    @Inject
    public EarningsViewModel(InstructorEarningsRepository earningsRepository) {
        this.earningsRepository = earningsRepository;
    }

    public void refreshEarnings(String page, String sort){
        earningsRepository.fetch(page, sort);
    }

    public LiveData<Resource<List<Earning>>> getEarnings() {
        return earningsRepository.getEarnings();
    }

    public LiveData<List<Page>> getPaginationData() {
        return earningsRepository.getEarningsPagination();
    }

    public void payout(){
        earningsRepository.payout(new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                payoutResult.setValue(state);
            }
        });
    }

    public MutableLiveData<String> getSort() {
        return sort;
    }

    public SingleLiveEvent<FormState<String>> getPayoutResult() {
        return payoutResult;
    }
}