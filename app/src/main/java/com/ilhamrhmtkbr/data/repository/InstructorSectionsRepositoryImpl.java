package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.local.entity.InstructorSectionsEntity;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.instructor.SectionMapper;
import com.ilhamrhmtkbr.data.remote.dto.request.InstructorSectionUpSertRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorSectionsResponse;
import com.ilhamrhmtkbr.data.local.dao.InstructorSectionsDao;
import com.ilhamrhmtkbr.data.remote.api.InstructorApi;
import com.ilhamrhmtkbr.domain.model.common.Section;
import com.ilhamrhmtkbr.domain.repository.InstructorSectionsRepository;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstructorSectionsRepositoryImpl implements InstructorSectionsRepository {
    private final InstructorApi api;
    private final InstructorSectionsDao sectionsDao;
    private final MediatorLiveData<Resource<List<Section>>> sectionsResult = new MediatorLiveData<>();

    @Inject
    public InstructorSectionsRepositoryImpl(InstructorApi api, InstructorSectionsDao sectionsDao) {
        this.api = api;
        this.sectionsDao = sectionsDao;
    }

    @Override
    public void fetch(String courseId) {
        sectionsResult.postValue(Resource.loading());
        api.sectionsFetch(courseId).enqueue(new Callback<BaseResponseApi<InstructorSectionsResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<InstructorSectionsResponse>> call, Response<BaseResponseApi<InstructorSectionsResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            sectionsDao.refresh(SectionMapper.fromResponseToEntities(response.body().data));
                        }
                    });
                    sectionsResult.postValue(Resource.success(null));
                } else if (response.errorBody() != null){
                    try {
                        sectionsResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<InstructorSectionsResponse>> call, Throwable t) {
                sectionsResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<List<Section>>> getSections(String courseId) {
        LiveData<List<InstructorSectionsEntity>> dbSource = sectionsDao.getAll();

        sectionsResult.addSource(dbSource, new Observer<List<InstructorSectionsEntity>>() {
            @Override
            public void onChanged(List<InstructorSectionsEntity> instructorSectionsEntities) {
                if (instructorSectionsEntities.isEmpty()) {
                    fetch(courseId);
                } else {
                    List<Section> sections = SectionMapper.fromEntitiesToList(instructorSectionsEntities);

                    if (sectionsResult.getValue() == null || !sectionsResult.getValue().isLoading()) {
                        sectionsResult.setValue(Resource.success(sections));
                    }
                }
            }
        });

        return sectionsResult;
    }

    @Override
    public void store(InstructorSectionUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.sectionStore(request).enqueue(new Callback<BaseResponseApi<Void>>() {
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
                callback.onResult(FormState.success(t.getMessage(), null));
            }
        });
    }

    @Override
    public void modify(InstructorSectionUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.sectionModify(request).enqueue(new Callback<BaseResponseApi<Void>>() {
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
                callback.onResult(FormState.success(t.getMessage(), null));
            }
        });
    }

    @Override
    public void delete(String sectionId, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.sectionDelete(sectionId).enqueue(new Callback<BaseResponseApi<Void>>() {
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
                callback.onResult(FormState.success(t.getMessage(), null));
            }
        });
    }
}
