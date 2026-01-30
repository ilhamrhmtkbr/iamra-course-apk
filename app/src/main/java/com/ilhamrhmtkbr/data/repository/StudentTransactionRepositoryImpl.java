package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.student.TransactionMapper;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Transaction;
import com.ilhamrhmtkbr.domain.repository.StudentTransactionRepository;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentTransactionCheckCouponStoreRequest;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentTransactionStoreRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentTransactionCheckCouponResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentTransactionDetailResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentTransactionResponse;
import com.ilhamrhmtkbr.data.local.dao.StudentTransactionsDao;
import com.ilhamrhmtkbr.data.local.dao.StudentTransactionsPagination;
import com.ilhamrhmtkbr.data.local.entity.StudentTransactionsEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentTransactionsPaginationEntity;
import com.ilhamrhmtkbr.data.remote.api.StudentApi;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentTransactionRepositoryImpl implements StudentTransactionRepository {
    private final MediatorLiveData<Resource<List<Transaction>>> transactionsResult = new MediatorLiveData<>();
    private final StudentApi api;
    private final StudentTransactionsDao studentTransactionsDao;
    private final StudentTransactionsPagination studentTransactionsPagination;
    private final SingleLiveEvent<Resource<StudentTransactionDetailResponse>> transactionDetailResult = new SingleLiveEvent<>();

    @Inject
    public StudentTransactionRepositoryImpl(StudentApi api, StudentTransactionsDao studentTransactionsDao, StudentTransactionsPagination studentTransactionsPagination) {
        this.api = api;
        this.studentTransactionsDao = studentTransactionsDao;
        this.studentTransactionsPagination = studentTransactionsPagination;
    }

    @Override
    public void fetch(String page, String sort, String status) {
        transactionsResult.postValue(Resource.loading());
        api.transactionsFetch(page, sort, status).enqueue(new Callback<BaseResponseApi<StudentTransactionResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<StudentTransactionResponse>> call, Response<BaseResponseApi<StudentTransactionResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            studentTransactionsDao.refresh(TransactionMapper.fromResponseToEntities(response.body().data));
                            studentTransactionsPagination.refresh(TransactionMapper.fromResponseToPaginationEntities(response.body().data));
                        }
                    });
                    transactionsResult.postValue(Resource.success(null));
                } else if (response.errorBody() != null) {
                    try {
                        transactionsResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<StudentTransactionResponse>> call, Throwable t) {
                transactionsResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<List<Transaction>>> getAllTransactions() {
        LiveData<List<StudentTransactionsEntity>> dbSource = studentTransactionsDao.getAll();
        transactionsResult.addSource(dbSource, new Observer<List<StudentTransactionsEntity>>() {
            @Override
            public void onChanged(List<StudentTransactionsEntity> studentTransactionsEntities) {
                if (studentTransactionsEntities.isEmpty()) {
                    fetch("1", "desc", "all");
                } else {
                    List<Transaction> transactions = TransactionMapper.fromEntitiesToList(studentTransactionsEntities);
                    if (transactionsResult.getValue() == null || !transactionsResult.getValue().isLoading()) {
                        transactionsResult.setValue(Resource.success(transactions));
                    }
                }
            }
        });
        return transactionsResult;
    }

    @Override
    public LiveData<List<Page>> getPaginationData() {
        return Transformations.map(studentTransactionsPagination.getAll(), entities -> {
            List<Page> pages = new ArrayList<>();
            for (StudentTransactionsPaginationEntity item : entities) {
                pages.add(new Page(item.url, item.label, item.isActive));
            }
            return pages;
        });
    }

    @Override
    public void show(String orderId) {
        transactionDetailResult.postValue(Resource.loading());
        api.transactionFetch(orderId).enqueue(new Callback<BaseResponseApi<StudentTransactionDetailResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<StudentTransactionDetailResponse>> call, Response<BaseResponseApi<StudentTransactionDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    transactionDetailResult.postValue(Resource.success(response.body().data));
                } else if (response.errorBody() != null) {
                    try {
                        transactionDetailResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<StudentTransactionDetailResponse>> call, Throwable t) {
                transactionDetailResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<StudentTransactionDetailResponse>> getTransactionDetailResult() {
        return transactionDetailResult;
    }

    @Override
    public void checkCoupon(StudentTransactionCheckCouponStoreRequest request, FormCallback<StudentTransactionCheckCouponResponse> callback) {
        callback.onResult(FormState.loading());
        api.transactionCheckCoupon(request).enqueue(new retrofit2.Callback<BaseResponseApi<StudentTransactionCheckCouponResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<StudentTransactionCheckCouponResponse>> call, Response<BaseResponseApi<StudentTransactionCheckCouponResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    callback.onResult(FormState.success(response.body().message, response.body().data));
                } else if (response.errorBody() != null){
                    ValidationErrorMapper<StudentTransactionCheckCouponResponse> validationErrorMapper = new ValidationErrorMapper<StudentTransactionCheckCouponResponse>();
                    try {
                        validationErrorMapper.handle(response.errorBody().string(), callback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    callback.onResult(FormState.error("error"));
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<StudentTransactionCheckCouponResponse>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    @Override
    public void store(StudentTransactionStoreRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.transactionStore(request).enqueue(new retrofit2.Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<String>();
                    try {
                        validationErrorMapper.handle(response.errorBody().string(), callback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<Void>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }

    @Override
    public void delete(String orderId, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.transactionDelete(orderId).enqueue(new retrofit2.Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null){
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<String>();
                    try {
                        validationErrorMapper.handle(response.errorBody().string(), callback);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<Void>> call, Throwable t) {
                callback.onResult(FormState.error(t.getMessage()));
            }
        });
    }
}
