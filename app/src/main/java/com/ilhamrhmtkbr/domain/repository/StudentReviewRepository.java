package com.ilhamrhmtkbr.domain.repository;

import androidx.lifecycle.LiveData;

import com.ilhamrhmtkbr.core.network.callback.FormCallback;
import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.data.remote.dto.request.StudentCourseReviewUpSertRequest;
import com.ilhamrhmtkbr.domain.model.common.Page;
import com.ilhamrhmtkbr.domain.model.student.Review;

import java.util.List;

public interface StudentReviewRepository {
    void fetch(String page, String sort);
    LiveData<Resource<List<Review>>> getReviews();
    LiveData<List<Page>> getPaginationData();
    void store(StudentCourseReviewUpSertRequest request, FormCallback<String> callback);
    void modify(StudentCourseReviewUpSertRequest request, FormCallback<String> callback);
    void delete(String reviewId, FormCallback<String> callback);
}
