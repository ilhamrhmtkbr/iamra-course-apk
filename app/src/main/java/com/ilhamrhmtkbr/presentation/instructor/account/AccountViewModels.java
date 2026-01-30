package com.ilhamrhmtkbr.presentation.instructor.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorAccountUpSertRequest;
import com.ilhamrhmtkbr.domain.model.instructor.Account;
import com.ilhamrhmtkbr.domain.repository.InstructorAccountRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AccountViewModels extends ViewModel implements ValidationFrontend<InstructorAccountUpSertRequest> {
    private final InstructorAccountRepository instructorAccountRepository;
    private final MutableLiveData<Map<String, String>> validationErrorsFrontend = new MutableLiveData<>();
    private final SingleLiveEvent<FormState<String>> accountFormState = new SingleLiveEvent<>();

    @Inject
    public AccountViewModels(InstructorAccountRepository instructorAccountRepository) {
        this.instructorAccountRepository = instructorAccountRepository;
    }

    public void refreshAccount() {
        instructorAccountRepository.fetch();
    }

    public LiveData<Resource<Account>> getAccount() {
        return instructorAccountRepository.getAccount();
    }

    public SingleLiveEvent<FormState<String>> getAccountFormState() {
        return accountFormState;
    }

    public void store(String accountId, String aliasName, String bankName) {
        InstructorAccountUpSertRequest request = new InstructorAccountUpSertRequest();
        request.accountId = accountId;
        request.aliasName = aliasName;
        request.bankName = bankName;

        if (isValidValue(request)) {
            instructorAccountRepository.store(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    accountFormState.postValue(state);
                }
            });
        }
    }

    public void modify(String accountId, String aliasName, String bankName) {
        InstructorAccountUpSertRequest request = new InstructorAccountUpSertRequest();
        request.accountId = accountId;
        request.aliasName = aliasName;
        request.bankName = bankName;

        if (isValidValue(request)) {
            instructorAccountRepository.modify(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    accountFormState.postValue(state);
                }
            });
        }
    }

    @Override
    public boolean isValidValue(InstructorAccountUpSertRequest request) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation validAccountId = new BaseValidation().validation("account_id", request.accountId).required().minMax(8, 12);
        BaseValidation validAliasName = new BaseValidation().validation("alias_name", request.aliasName).required().minMax(8, 12);
        BaseValidation validBankName = new BaseValidation().validation("bank_name", request.bankName).required();

        if (validAccountId.hasError()) {
            errors.put(validAccountId.getFieldName(), validAccountId.getError());
        }
        if (validAliasName.hasError()) {
            errors.put(validAliasName.getFieldName(), validAliasName.getError());
        }
        if (validBankName.hasError()) {
            errors.put(validBankName.getFieldName(), validBankName.getError());
        }

        validationErrorsFrontend.setValue(errors);

        return errors.isEmpty();
    }

    public LiveData<Map<String, String>> getValidationErrorsFrontend() {
        return validationErrorsFrontend;
    }
}
