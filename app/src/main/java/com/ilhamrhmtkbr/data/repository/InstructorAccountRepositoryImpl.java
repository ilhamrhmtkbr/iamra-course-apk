package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.instructor.AccountMapper;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorAccountUpSertRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorAccountResponse;
import com.ilhamrhmtkbr.data.local.dao.InstructorAccountDao;
import com.ilhamrhmtkbr.data.local.entity.InstructorAccountEntity;
import com.ilhamrhmtkbr.data.remote.api.InstructorApi;
import com.ilhamrhmtkbr.domain.model.instructor.Account;
import com.ilhamrhmtkbr.domain.repository.InstructorAccountRepository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructorAccountRepositoryImpl implements InstructorAccountRepository {
    private final InstructorApi api;
    private final InstructorAccountDao accountDao;
    private final MediatorLiveData<Resource<Account>> accountResult = new MediatorLiveData<>();

    @Inject
    public InstructorAccountRepositoryImpl(InstructorApi instructorApi, InstructorAccountDao accountDao) {
        this.api = instructorApi;
        this.accountDao = accountDao;
    }

    @Override
    public void fetch() {
        accountResult.postValue(Resource.loading());
        api.accountFetch().enqueue(new Callback<BaseResponseApi<InstructorAccountResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<InstructorAccountResponse>> call, Response<BaseResponseApi<InstructorAccountResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    accountResult.postValue(Resource.success(null));
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            accountDao.refresh(AccountMapper.fromResponseToEntities(response.body().data));
                        }
                    });
                } else if (response.errorBody() != null) {
                    try {
                        accountResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    accountResult.postValue(Resource.success(null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<InstructorAccountResponse>> call, Throwable t) {
                accountResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<Account>> getAccount() {
        LiveData<List<InstructorAccountEntity>> dbSource = accountDao.getAll();

        accountResult.addSource(dbSource, new Observer<List<InstructorAccountEntity>>() {
            @Override
            public void onChanged(List<InstructorAccountEntity> instructorAccountEntities) {
                if (instructorAccountEntities.isEmpty()) {
                    fetch();
                } else {
                    Account account = AccountMapper.fromEntityListToModel(instructorAccountEntities);
                    if (accountResult.getValue() == null || !accountResult.getValue().isLoading()) {
                        accountResult.setValue(Resource.success(account));
                    }
                }
            }
        });

        return accountResult;
    }

    @Override
    public void store(InstructorAccountUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());

        api.accountStore(request).enqueue(new Callback<BaseResponseApi<String>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<String>> call, Response<BaseResponseApi<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    try {
                        String errorBody = response.errorBody().string();
                        ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                        validationErrorMapper.handle(errorBody, callback);
                    } catch (Exception e) {
                        callback.onResult(FormState.error(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<String>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    @Override
    public void modify(InstructorAccountUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());

        api.accountModify(request).enqueue(new Callback<BaseResponseApi<String>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<String>> call, Response<BaseResponseApi<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    try {
                        String errorBody = response.errorBody().string();
                        ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
                        validationErrorMapper.handle(errorBody, callback);
                    } catch (Exception e) {
                        callback.onResult(FormState.error(e.getMessage()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<String>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }
}
