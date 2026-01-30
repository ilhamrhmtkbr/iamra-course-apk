package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.instructor.EarningMapper;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Earning;
import com.ilhamrhmtkbr.domain.repository.InstructorEarningsRepository;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorEarningsResponse;
import com.ilhamrhmtkbr.data.local.dao.InstructorEarningsDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorEarningsPagination;
import com.ilhamrhmtkbr.data.local.entity.InstructorEarningsEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorEarningsPaginationEntity;
import com.ilhamrhmtkbr.data.remote.api.InstructorApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructorEarningsRepositoryImpl implements InstructorEarningsRepository {
    private final InstructorApi api;
    private final InstructorEarningsDao earningsDao;
    private final InstructorEarningsPagination earningsPagination;
    private final MediatorLiveData<Resource<List<Earning>>> earningResult = new MediatorLiveData<>();

    @Inject
    public InstructorEarningsRepositoryImpl(InstructorApi api, InstructorEarningsDao earningsDao, InstructorEarningsPagination earningsPagination) {
        this.api = api;
        this.earningsDao = earningsDao;
        this.earningsPagination = earningsPagination;
    }

    @Override
    public void fetch(String page, String sort) {
        earningResult.postValue(Resource.loading());
        api.earningsFetch(page, sort).enqueue(new Callback<InstructorEarningsResponse>() {
            @Override
            public void onResponse(Call<InstructorEarningsResponse> call, Response<InstructorEarningsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            earningsDao.refresh(EarningMapper.fromResponseToEarningListEntities(response.body()));
                            earningsPagination.refresh(EarningMapper.fromResponseToEarningListPaginationEntities(response.body()));
                        }
                    });
                    earningResult.postValue(Resource.success(null));
                } else if (response.errorBody() != null){
                    try {
                        earningResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<InstructorEarningsResponse> call, Throwable t) {
                earningResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<List<Earning>>> getEarnings() {
        LiveData<List<InstructorEarningsEntity>> dbSource = earningsDao.getAll();

        earningResult.addSource(dbSource, new Observer<List<InstructorEarningsEntity>>() {
            @Override
            public void onChanged(List<InstructorEarningsEntity> instructorEarningsEntities) {
                if (instructorEarningsEntities.isEmpty()) {
                    fetch("1", "desc");
                } else {
                    List<Earning> earnings = EarningMapper.fromEntitiesToList(instructorEarningsEntities);
                    if (earningResult.getValue() == null || !earningResult.getValue().isLoading()) {
                        earningResult.setValue(Resource.success(earnings));
                    }
                }
            }
        });

        return earningResult;
    }

    @Override
    public LiveData<List<Page>> getEarningsPagination() {
        return Transformations.map(earningsPagination.getAll(), new Function1<List<InstructorEarningsPaginationEntity>, List<Page>>() {
            @Override
            public List<Page> invoke(List<InstructorEarningsPaginationEntity> instructorEarningsPaginationEntities) {
                List<Page> pages = new ArrayList<>();
                for (InstructorEarningsPaginationEntity item : instructorEarningsPaginationEntities) {
                    pages.add(new Page(item.url, item.label, item.isActive));
                }
                return pages;
            }
        });
    }

    @Override
    public void payout(FormCallback<String> callback) {
        callback.onResult(FormState.loading());

        api.earningPayout().enqueue(new Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null){
                    try {
                        callback.onResult(FormState.error(response.errorBody().string()));
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
