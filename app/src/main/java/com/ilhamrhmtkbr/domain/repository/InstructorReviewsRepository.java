package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.instructor.Review;

import java.util.List;

public interface InstructorReviewsRepository {
    void fetch(String page, String sort);
    LiveData<Resource<List<Review>>> getReviews();
    LiveData<List<Page>> getReviewsPagination();
}
