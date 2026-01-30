package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.student.ProgressMapper;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Progress;
import com.ilhamrhmtkbr.domain.model.student.ProgressDetail;
import com.ilhamrhmtkbr.domain.repository.StudentProgressesRepository;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCourseProgressStoreRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentProgressResponse;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentProgressesResponse;
import com.ilhamrhmtkbr.data.local.dao.StudentProgressesDao;
import com.ilhamrhmtkbr.data.local.dao.StudentProgressesPagination;
import com.ilhamrhmtkbr.data.local.entity.StudentProgressesEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentProgressesPaginationEntity;
import com.ilhamrhmtkbr.data.remote.api.StudentApi;
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

public class StudentProgressesRepositoryImpl implements StudentProgressesRepository {
    private final MediatorLiveData<Resource<List<Progress>>> progressesResult = new MediatorLiveData<>();
    private final StudentProgressesDao progressesDao;
    private final StudentProgressesPagination studentProgressesPagination;
    private final StudentApi api;
    private final SingleLiveEvent<Resource<ProgressDetail>> progressDetailResult = new SingleLiveEvent<>();

    @Inject
    public StudentProgressesRepositoryImpl(StudentProgressesDao progressesDao, StudentProgressesPagination studentProgressesPagination, StudentApi api) {
        this.progressesDao = progressesDao;
        this.studentProgressesPagination = studentProgressesPagination;
        this.api = api;
    }

    @Override
    public void fetch(String page) {
        progressesResult.postValue(Resource.loading());
        api.courseProgressesFetch(page).enqueue(new Callback<BaseResponseApi<StudentProgressesResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<StudentProgressesResponse>> call, Response<BaseResponseApi<StudentProgressesResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            progressesDao.refresh(ProgressMapper.fromResponseToEntities(response.body().data));
                            studentProgressesPagination.refresh(ProgressMapper.fromResponseToPaginationEntities(response.body().data));
                        }
                    });
                    progressesResult.postValue(Resource.success(null));
                } else if (response.errorBody() != null) {
                    try {
                        progressesResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<StudentProgressesResponse>> call, Throwable t) {
                progressesResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<List<Progress>>> getAllProgresses() {
        LiveData<List<StudentProgressesEntity>> dbSource = progressesDao.getAllProgresses();
        progressesResult.addSource(dbSource, new Observer<List<StudentProgressesEntity>>() {
            @Override
            public void onChanged(List<StudentProgressesEntity> studentProgressesEntities) {
                if (studentProgressesEntities.isEmpty()) {
                    fetch("1");
                } else {
                    List<Progress> progresses = ProgressMapper.fromEntitiesToList(studentProgressesEntities);
                    if (progressesResult.getValue() == null || !progressesResult.getValue().isLoading()) {
                        progressesResult.setValue(Resource.success(progresses));
                    }
                }
            }
        });

        return progressesResult;
    }

    @Override
    public LiveData<List<Page>> getAllPagination() {
        return Transformations.map(studentProgressesPagination.getAll(), new Function1<List<StudentProgressesPaginationEntity>, List<Page>>() {
            @Override
            public List<Page> invoke(List<StudentProgressesPaginationEntity> studentProgressesPaginationEntities) {
                List<Page> pages = new ArrayList<>();
                for (StudentProgressesPaginationEntity item : studentProgressesPaginationEntities) {
                    pages.add(new Page(item.url, item.label, item.isActive));
                }
                return pages;
            }
        });
    }

    @Override
    public void show(String courseId) {
        progressDetailResult.postValue(Resource.loading());
        api.courseProgressFetch(courseId).enqueue(new Callback<BaseResponseApi<StudentProgressResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<StudentProgressResponse>> call, Response<BaseResponseApi<StudentProgressResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    progressDetailResult.postValue(Resource.success(ProgressMapper.fromResponseToProgressDetail(response.body().data)));
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<StudentProgressResponse>> call, Throwable t) {
                progressDetailResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public SingleLiveEvent<Resource<ProgressDetail>> getProgressDetailResult() {
        return progressDetailResult;
    }

    @Override
    public void store(StudentCourseProgressStoreRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.courseProgressStore(request).enqueue(new Callback<BaseResponseApi<Void>>() {
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
