package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.local.entity.InstructorSocialsEntity;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.instructor.SocialMapper;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorSocialUpSertRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorSocialsResponse;
import com.ilhamrhmtkbr.data.local.dao.InstructorSocialsDao;
import com.ilhamrhmtkbr.data.remote.api.InstructorApi;
import com.ilhamrhmtkbr.domain.model.instructor.Social;
import com.ilhamrhmtkbr.domain.repository.InstructorSocialsRepository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructorSocialsRepositoryImpl implements InstructorSocialsRepository {
    private final InstructorApi api;
    private final InstructorSocialsDao socialsDao;
    private final MediatorLiveData<Resource<List<Social>>> socialsResult = new MediatorLiveData<>();

    @Inject
    public InstructorSocialsRepositoryImpl(InstructorApi api, InstructorSocialsDao socialsDao) {
        this.api = api;
        this.socialsDao = socialsDao;
    }

    public void fetch() {
        socialsResult.postValue(Resource.loading());
        api.socialsFetch().enqueue(new Callback<InstructorSocialsResponse>() {
            @Override
            public void onResponse(Call<InstructorSocialsResponse> call, Response<InstructorSocialsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            socialsDao.refresh(SocialMapper.fromResponseToEntities(response.body()));
                        }
                    });
                    socialsResult.postValue(Resource.success(null));
                } else if (response.errorBody() != null) {
                    try {
                        socialsResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<InstructorSocialsResponse> call, Throwable t) {
                socialsResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    public MediatorLiveData<Resource<List<Social>>> getSocials() {
        LiveData<List<InstructorSocialsEntity>> dbSource = socialsDao.getAll();

        socialsResult.addSource(dbSource, new Observer<List<InstructorSocialsEntity>>() {
            @Override
            public void onChanged(List<InstructorSocialsEntity> instructorSocialsEntities) {
                if (instructorSocialsEntities.isEmpty()) {
                    fetch();
                } else {
                    List<Social> socials = SocialMapper.fromEntitiesToList(instructorSocialsEntities);
                    if (socialsResult.getValue() == null || !socialsResult.getValue().isLoading()) {
                        socialsResult.setValue(Resource.success(socials));
                    }
                }
            }
        });

        return socialsResult;
    }

    public void store(InstructorSocialUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.socialStore(request).enqueue(new Callback<BaseResponseApi<Void>>() {
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

    public void modify(InstructorSocialUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.socialModify(request).enqueue(new Callback<BaseResponseApi<Void>>() {
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

    public void delete(String id, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.socialDelete(id).enqueue(new Callback<BaseResponseApi<Void>>() {
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
