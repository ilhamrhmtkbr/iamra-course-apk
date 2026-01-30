package com.ilhamrhmtkbr.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.local.entity.InstructorReviewsEntity;
import com.ilhamrhmtkbr.data.mapper.instructor.ReviewMapper;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Review;
import com.ilhamrhmtkbr.domain.repository.InstructorReviewsRepository;
import com.ilhamrhmtkbr.data.remote.dto.response.InstructorReviewsResponse;
import com.ilhamrhmtkbr.data.local.dao.InstructorReviewsDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorReviewsPagination;
import com.ilhamrhmtkbr.data.local.entity.InstructorReviewsPaginationEntity;
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

public class InstructorReviewsRepositoryImpl implements InstructorReviewsRepository {
    private final InstructorApi api;
    private final InstructorReviewsDao reviewsDao;
    private final InstructorReviewsPagination reviewsPagination;
    private final MediatorLiveData<Resource<List<Review>>> reviewsResult = new MediatorLiveData<>();

    @Inject
    public InstructorReviewsRepositoryImpl(InstructorApi api, InstructorReviewsDao reviewsDao, InstructorReviewsPagination reviewsPagination) {
        this.api = api;
        this.reviewsDao = reviewsDao;
        this.reviewsPagination = reviewsPagination;
    }

    @Override
    public void fetch(String page, String sort) {
        reviewsResult.setValue(Resource.loading());
        api.courseReviewsFetch(page, sort).enqueue(new Callback<InstructorReviewsResponse>() {
            @Override
            public void onResponse(Call<InstructorReviewsResponse> call, Response<InstructorReviewsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            reviewsDao.refresh(ReviewMapper.fromResponseToEntitiesList(response.body()));
                            reviewsPagination.refresh(ReviewMapper.fromResponseToPaginationEntitiesList(response.body()));
                        }
                    });
                    reviewsResult.setValue(Resource.success(null));
                } else if (response.errorBody() != null){
                    try {
                        reviewsResult.postValue(Resource.error(response.errorBody().string()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<InstructorReviewsResponse> call, Throwable t) {
                reviewsResult.setValue(Resource.error(t.getMessage()));
            }
        });
    }

    @Override
    public LiveData<Resource<List<Review>>> getReviews() {
        LiveData<List<InstructorReviewsEntity>> dbSource = reviewsDao.getAll();

        reviewsResult.addSource(dbSource, new Observer<List<InstructorReviewsEntity>>() {
            @Override
            public void onChanged(List<InstructorReviewsEntity> instructorReviewsEntities) {
                if (instructorReviewsEntities.isEmpty()){
                    fetch("1", "desc");
                } else {
                    List<Review> reviews = ReviewMapper.fromEntitiesToList(instructorReviewsEntities);
                    if (reviewsResult.getValue() == null || !reviewsResult.getValue().isLoading()) {
                        reviewsResult.setValue(Resource.success(reviews));
                    }
                }
            }
        });

        return reviewsResult;
    }

    @Override
    public LiveData<List<Page>> getReviewsPagination() {
        return Transformations.map(reviewsPagination.getAll(), new Function1<List<InstructorReviewsPaginationEntity>, List<Page>>() {
            @Override
            public List<Page> invoke(List<InstructorReviewsPaginationEntity> instructorReviewsPaginationEntities) {
                List<Page> pages = new ArrayList<>();

                for (InstructorReviewsPaginationEntity item : instructorReviewsPaginationEntities ) {
                    pages.add(new Page(item.url, item.label, item.isActive));
                }
                return pages;
            }
        });
    }

}
