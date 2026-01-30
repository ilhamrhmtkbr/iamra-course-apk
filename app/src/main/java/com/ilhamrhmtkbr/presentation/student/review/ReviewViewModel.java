package com.ilhamrhmtkbr.presentation.student.review;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.base.BaseValidation;
import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.FormState;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCourseReviewUpSertRequest;
import com.ilhamrhmtkbr.domain.repository.StudentReviewRepository;
import com.ilhamrhmtkbr.presentation.utils.tools.SingleLiveEvent;
import com.ilhamrhmtkbr.presentation.utils.tools.ValidationFrontend;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReviewViewModel extends ViewModel implements ValidationFrontend<StudentCourseReviewUpSertRequest> {
    private final StudentReviewRepository reviewRepository;
    private final SingleLiveEvent<FormState<String>> reviewFormState = new SingleLiveEvent<>();
    private final MutableLiveData<Map<String, String>> validationErrorsFrontend = new MutableLiveData<>();

    @Inject
    public ReviewViewModel(StudentReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void store(String courseId, String review, String rating) {
        StudentCourseReviewUpSertRequest request = new StudentCourseReviewUpSertRequest();
        request.instructorCourseId = courseId;
        request.review = review;
        request.rating = rating;

        if (isValidValue(request)) {
            reviewRepository.store(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    reviewFormState.postValue(state);
                }
            });
        }
    }

    public void modify(String courseId, String review, String rating) {
        StudentCourseReviewUpSertRequest request = new StudentCourseReviewUpSertRequest();
        request.instructorCourseId = courseId;
        request.review = review;
        request.rating = rating;

        if (isValidValue(request)) {
            reviewRepository.modify(request, new FormCallback<String>() {
                @Override
                public void onResult(FormState<String> state) {
                    reviewFormState.postValue(state);
                }
            });
        }
    }

    public void delete(String reviewId) {
        reviewRepository.delete(reviewId, new FormCallback<String>() {
            @Override
            public void onResult(FormState<String> state) {
                reviewFormState.postValue(state);
            }
        });
    }

    public void refresh(){
        reviewRepository.fetch("1", "desc");
    }

    @Override
    public boolean isValidValue(StudentCourseReviewUpSertRequest request) {
        Map<String, String> errors = new HashMap<>();

        BaseValidation review = new BaseValidation().validation("review", request.review).required().minMax(10, 125);
        BaseValidation rating = new BaseValidation().validation("rating", request.rating).required();

        if (review.hasError()) {
            errors.put(review.getFieldName(), review.getError());
        }

        if (rating.hasError()) {
            errors.put(rating.getFieldName(), rating.getError());
        }

        validationErrorsFrontend.setValue(errors);

        return errors.isEmpty();
    }

    public SingleLiveEvent<FormState<String>> getReviewFormState() {
        return reviewFormState;
    }

    public MutableLiveData<Map<String, String>> getValidationErrorsFrontend() {
        return validationErrorsFrontend;
    }
}
