package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.student.CartMapper;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Cart;
import com.ilhamrhmtkbr.domain.repository.StudentCartRepository;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCartStoreRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentCartResponse;
import com.ilhamrhmtkbr.data.local.dao.StudentCartsDao;
import com.ilhamrhmtkbr.data.local.dao.StudentCartsPagination;
import com.ilhamrhmtkbr.data.local.entity.StudentCartsEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentCartsPaginationEntity;
import com.ilhamrhmtkbr.data.remote.api.StudentApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentCartRepositoryImpl implements StudentCartRepository {
    private final MediatorLiveData<Resource<List<Cart>>> cartsResult = new MediatorLiveData<>();
    private final StudentApi api;
    private final StudentCartsDao studentCartsDao;
    private final StudentCartsPagination studentCartsPagination;

    @Inject
    public StudentCartRepositoryImpl(StudentApi api, StudentCartsDao studentCartsDao, StudentCartsPagination studentCartsPagination) {
        this.api = api;
        this.studentCartsDao = studentCartsDao;
        this.studentCartsPagination = studentCartsPagination;
    }

    @Override
    public void fetch(String page, String sort) {
        cartsResult.postValue(Resource.loading());

        api.cartsFetch(page, sort).enqueue(new Callback<BaseResponseApi<StudentCartResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<StudentCartResponse>> call, Response<BaseResponseApi<StudentCartResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            studentCartsDao.refresh(CartMapper.fromResponseToEntities(response.body().data));
                            studentCartsPagination.refresh(CartMapper.fromResponseToPaginationEntities(response.body().data));
                        }
                    });
                    cartsResult.postValue(Resource.success(null));
                } else if (response.errorBody() != null){
                    try {
                        cartsResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<StudentCartResponse>> call, Throwable t) {
                cartsResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<List<Page>> getPaginationData() {
        return Transformations.map(studentCartsPagination.getAll(), entities -> {
            List<Page> pages = new ArrayList<>();
            for (StudentCartsPaginationEntity item : entities) {
                pages.add(new Page(item.url, item.label, item.isActive));
            }
            return pages;
        });
    }

    @Override
    public LiveData<Resource<List<Cart>>> getAllCarts() {
        LiveData<List<StudentCartsEntity>> dbSource = studentCartsDao.getAll();

        cartsResult.addSource(dbSource, new Observer<List<StudentCartsEntity>>() {
            @Override
            public void onChanged(List<StudentCartsEntity> studentCartsEntities) {
                if (studentCartsEntities.isEmpty()) {
                    fetch("1", "desc");
                } else {
                    List<Cart> carts = CartMapper.fromEntitiesToList(studentCartsEntities);
                    if (cartsResult.getValue() == null || !cartsResult.getValue().isLoading()) {
                        cartsResult.setValue(Resource.success(carts));
                    }
                }
            }
        });

        return cartsResult;
    }

    @Override
    public void store(StudentCartStoreRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.cartStore(request).enqueue(new Callback<BaseResponseApi<Void>>() {
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
    public void delete(String cartId, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.cartDelete(cartId).enqueue(new retrofit2.Callback<BaseResponseApi<Void>>() {
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
}
