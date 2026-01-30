package com.ilhamrhmtkbr.presentation.instructor.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.local.entity.InstructorReviewsEntity;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Review;
import com.ilhamrhmtkbr.domain.repository.InstructorReviewsRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReviewsViewModel extends ViewModel {
    private final InstructorReviewsRepository reviewsRepository;
    private final MutableLiveData<String> sort = new MutableLiveData<>();

    @Inject
    public ReviewsViewModel(InstructorReviewsRepository reviewsRepository) {
        this.reviewsRepository = reviewsRepository;
    }

    public void refreshReviews(String page, String sort ){
        reviewsRepository.fetch(page, sort);
    }

    public LiveData<Resource<List<Review>>> getReviews() {
        return reviewsRepository.getReviews();
    }

    public LiveData<List<Page>> getPaginationData() {
        return reviewsRepository.getReviewsPagination();
    }

    public MutableLiveData<String> getSort() {
        return sort;
    }

}