package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.mapper.ValidationErrorMapper;
import com.ilhamrhmtkbr.data.mapper.student.ReviewMapper;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Review;
import com.ilhamrhmtkbr.domain.repository.StudentReviewRepository;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCourseReviewUpSertRequest;
import com.ilhamrhmtkbr.core.base.BaseResponseApi;
import com.ilhamrhmtkbr.data.remote.dto.response.StudentReviewsResponse;
import com.ilhamrhmtkbr.data.local.dao.StudentReviewsDao;
import com.ilhamrhmtkbr.data.local.dao.StudentReviewsPagination;
import com.ilhamrhmtkbr.data.local.entity.StudentReviewsEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentReviewsPaginationEntity;
import com.ilhamrhmtkbr.data.remote.api.StudentApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentReviewRepositoryImpl implements StudentReviewRepository {
    private final MediatorLiveData<Resource<List<Review>>> reviewsResult = new MediatorLiveData<>();
    private final StudentApi api;
    private final StudentReviewsDao studentReviewsDao;
    private final StudentReviewsPagination studentReviewsPagination;

    @Inject
    public StudentReviewRepositoryImpl(StudentApi api, StudentReviewsDao studentReviewsDao, StudentReviewsPagination studentReviewsPagination) {
        this.api = api;
        this.studentReviewsDao = studentReviewsDao;
        this.studentReviewsPagination = studentReviewsPagination;
    }

    @Override
    public void fetch(String page, String sort) {
        reviewsResult.postValue(Resource.loading());
        api.courseReviewsFetch(page, sort).enqueue(new Callback<BaseResponseApi<StudentReviewsResponse>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<StudentReviewsResponse>> call, Response<BaseResponseApi<StudentReviewsResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            studentReviewsDao.refresh(ReviewMapper.fromResponseToEntities(response.body().data));
                            studentReviewsPagination.refresh(ReviewMapper.fromResponseToPaginationEntities(response.body().data));
                        }
                    });
                    reviewsResult.postValue(Resource.success(null));
                } else if (response.errorBody() != null){
                    try {
                        reviewsResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponseApi<StudentReviewsResponse>> call, Throwable t) {
                reviewsResult.postValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<List<Review>>> getReviews() {
        LiveData<List<StudentReviewsEntity>> dbSource = studentReviewsDao.getAll();
        reviewsResult.addSource(dbSource, new Observer<List<StudentReviewsEntity>>() {
            @Override
            public void onChanged(List<StudentReviewsEntity> studentReviewsEntities) {
                if (studentReviewsEntities.isEmpty()) {
                    fetch("1", "desc");
                } else {
                    List<Review> reviews = ReviewMapper.fromEntitiesToList(studentReviewsEntities);
                    if (reviewsResult.getValue() == null || !reviewsResult.getValue().isLoading()) {
                        reviewsResult.setValue(Resource.success(reviews));
                    }
                }
            }
        });
        return reviewsResult;
    }

    @Override
    public LiveData<List<Page>> getPaginationData() {
        return Transformations.map(studentReviewsPagination.getAll(), new Function1<List<StudentReviewsPaginationEntity>, List<Page>>() {
            @Override
            public List<Page> invoke(List<StudentReviewsPaginationEntity> studentReviewsPaginationEntities) {
                List<Page> pages = new ArrayList<>();
                for (StudentReviewsPaginationEntity item : studentReviewsPaginationEntities) {
                    pages.add(new Page(item.url, item.label, item.isActive));
                }

                return pages;
            }
        });
    }

    @Override
    public void store(StudentCourseReviewUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.courseReviewStore(request).enqueue(new retrofit2.Callback<BaseResponseApi<Void>>() {
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
    public void modify(StudentCourseReviewUpSertRequest request, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.courseReviewModify(request).enqueue(new retrofit2.Callback<BaseResponseApi<Void>>() {
            @Override
            public void onResponse(Call<BaseResponseApi<Void>> call, Response<BaseResponseApi<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(FormState.success(response.body().message, null));
                } else if (response.errorBody() != null) {
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
    public void delete(String reviewId, FormCallback<String> callback) {
        callback.onResult(FormState.loading());
        api.courseReviewDelete(reviewId).enqueue(new retrofit2.Callback<BaseResponseApi<Void>>() {
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
