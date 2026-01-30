package com.ilhamrhmtkbr.presentation.instructor.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.instructor.Answer;
import com.ilhamrhmtkbr.domain.model.instructor.Coupon;
import com.ilhamrhmtkbr.domain.model.instructor.CoursesLike;
import com.ilhamrhmtkbr.domain.model.instructor.Earning;
import com.ilhamrhmtkbr.domain.model.instructor.Review;
import com.ilhamrhmtkbr.domain.model.instructor.Social;
import com.ilhamrhmtkbr.domain.repository.InstructorAnswersRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorCouponsRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorCoursesRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorEarningsRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorReviewsRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorSocialsRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProfileViewModel extends ViewModel {
    private final InstructorAnswersRepository answersRepository;
    private final InstructorCouponsRepository couponsRepository;
    private final InstructorCoursesRepository coursesRepository;
    private final InstructorEarningsRepository earningsRepository;
    private final InstructorReviewsRepository reviewsRepository;
    private final InstructorSocialsRepository socialsRepository;

    @Inject
    public ProfileViewModel(InstructorAnswersRepository answersRepository, InstructorCouponsRepository couponsRepository, InstructorCoursesRepository coursesRepository, InstructorEarningsRepository earningsRepository, InstructorReviewsRepository reviewsRepository, InstructorSocialsRepository socialsRepository) {
        this.answersRepository = answersRepository;
        this.couponsRepository = couponsRepository;
        this.coursesRepository = coursesRepository;
        this.earningsRepository = earningsRepository;
        this.reviewsRepository = reviewsRepository;
        this.socialsRepository = socialsRepository;
    }

    public LiveData<Resource<List<Answer>>> getAnswers(){
        return answersRepository.getAnswers();
    }

    public LiveData<Resource<List<Coupon>>> getCoupons() {
        return couponsRepository.getCoupons();
    }

    public LiveData<Resource<List<CoursesLike>>> getCoursesLikes() {
        return coursesRepository.getCoursesLikes();
    }

    public LiveData<Resource<List<Earning>>> getEarnings(){
        return earningsRepository.getEarnings();
    }

    public LiveData<Resource<List<Review>>> getReviews() {
        return reviewsRepository.getReviews();
    }

    public LiveData<Resource<List<Social>>> getSocials(){
        return socialsRepository.getSocials();
    }
}
