package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.instructor.CouponsMapper;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Coupon;
import com.ilhamrhmtkbr.domain.repository.InstructorCouponsRepository;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorCouponUpSertRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCouponResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorCouponsResponse;
import com.ilhamrhmtkbr.data.local.dao.InstructorCouponsDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorCouponsPagination;
import com.ilhamrhmtkbr.data.local.entity.InstructorCouponsPaginationEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorCouponsEntity;
import com.ilhamrhmtkbr.data.remote.api.InstructorApi;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructorCouponsRepositoryImpl implements InstructorCouponsRepository {
    private final InstructorApi api;
    private final InstructorCouponsDao couponsDao;
    private final InstructorCouponsPagination couponsPagination;
    private final MediatorLiveData<Resource<List<Coupon>>> couponsResult = new MediatorLiveData<>();
    private final SingleLiveEvent<Resource<Coupon>> couponResult = new SingleLiveEvent<>();

    @Inject
    public InstructorCouponsRepositoryImpl(InstructorApi api, InstructorCouponsDao couponsDao, InstructorCouponsPagination couponsPagination) {
        this.api = api;
        this.couponsDao = couponsDao;
        this.couponsPagination = couponsPagination;
    }

    @Override
    public void fetch(String page, String sort) {
        couponsResult.postValue(Resource.loading());
        api.couponsFetch(page, sort).enqueue(new Callback<InstructorCouponsResponse>() {
            @Override
            public void onResponse(Call<InstructorCouponsResponse> call, Response<InstructorCouponsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            couponsDao.refresh(CouponsMapper.fromResponseToEntities(response.body()));
                            couponsPagination.refresh(CouponsMapper.fromResponseToPaginationEntities(response.body()));
                        }
                    });
                    couponsResult.setValue(Resource.success(null));
                } else if (response.errorBody() != null) {
                    try {
                        couponsResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<InstructorCouponsResponse> call, Throwable t) {
                couponsResult.setValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<List<Coupon>>> getCoupons() {
        LiveData<List<InstructorCouponsEntity>> dbSource = couponsDao.getAll();

        couponsResult.addSource(dbSource, new Observer<List<InstructorCouponsEntity>>() {
            @Override
            public void onChanged(List<InstructorCouponsEntity> instructorCouponsEntities) {
                if (instructorCouponsEntities.isEmpty()) {
                    fetch("1", "desc");
                } else {
                    List<Coupon> coupons = CouponsMapper.fromEntitiesToList(instructorCouponsEntities);

                    if (couponsResult.getValue() == null || !couponsResult.getValue().isLoading()) {
                        couponsResult.setValue(Resource.success(coupons));
                    }
                }
            }
        });

        return couponsResult;
    }

    @Override
    public LiveData<List<Page>> getCouponsPagination() {
        return Transformations.map(couponsPagination.getAll(), new Function1<List<InstructorCouponsPaginationEntity>, List<Page>>() {
            @Override
            public List<Page> invoke(List<InstructorCouponsPaginationEntity> instructorAnswersPaginationEntities) {
                List<Page> pages = new ArrayList<>();
                for (InstructorCouponsPaginationEntity item : instructorAnswersPaginationEntities) {
                    pages.add(new Page(item.url, item.label, item.isActive));
                }

                return pages;
            }
        });
    }

    @Override
    public void show(String couponId) {
        couponResult.postValue(Resource.loading());
        api.couponFetch(couponId).enqueue(new Callback<InstructorCouponResponse>() {
            @Override
            public void onResponse(Call<InstructorCouponResponse> call, Response<InstructorCouponResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    couponResult.postValue(Resource.success(CouponsMapper.fromResponseToModel(response.body())));
                } else {
                    try {
                        couponResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<InstructorCouponResponse> call, Throwable t) {
                couponResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public SingleLiveEvent<Resource<Coupon>> getCouponResult() {
        return couponResult;
    }

    @Override
    public void store(InstructorCouponUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());

        api.couponStore(request).enqueue(new Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
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
    public void modify(InstructorCouponUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.couponModify(request).enqueue(new Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
                    ValidationErrorMapper<String> validationErrorMapper = new ValidationErrorMapper<>();
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
    public void delete(String couponId, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.couponDelete(couponId).enqueue(new Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        callback.onResult(FormState.success(response.body().message, null));
                    } else if (response.errorBody() != null) {
                        try {
                            callback.onResult(FormState.error(response.errorBody().string()));
                        } catch (IOException e) {
                            callback.onResult(FormState.error(e.getMessage()));
                        }
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
