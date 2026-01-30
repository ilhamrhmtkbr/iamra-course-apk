package com.ilhamrhmtkbr.presentation.student.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Review;
import com.ilhamrhmtkbr.domain.repository.StudentReviewRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReviewsViewModel extends ViewModel {
    private final StudentReviewRepository reviewRepository;
    private final MutableLiveData<String> sort = new MutableLiveData<>();

    @Inject
    public ReviewsViewModel(StudentReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public LiveData<Resource<List<Review>>> getReviews() {
        return reviewRepository.getReviews();
    }

    public void refreshReviews(String page, String sort) {
        reviewRepository.fetch(page, sort);
    }

    public MutableLiveData<String> getSort() {
        return sort;
    }

    public LiveData<List<Page>> getPaginationData() {
        return reviewRepository.getPaginationData();
    }
}
