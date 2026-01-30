package com.ilhamrhmtkbr.presentation.student.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ilhamrhmtkbr.core.state.Resource;
import com.ilhamrhmtkbr.domain.model.common.Certificate;
import com.ilhamrhmtkbr.domain.model.common.Course;
import com.ilhamrhmtkbr.domain.model.student.Cart;
import com.ilhamrhmtkbr.domain.model.student.Question;
import com.ilhamrhmtkbr.domain.model.student.Review;
import com.ilhamrhmtkbr.domain.model.student.Transaction;
import com.ilhamrhmtkbr.domain.repository.StudentCartRepository;
import com.ilhamrhmtkbr.domain.repository.StudentCertificateRepository;
import com.ilhamrhmtkbr.domain.repository.StudentQuestionRepository;
import com.ilhamrhmtkbr.domain.repository.StudentReviewRepository;
import com.ilhamrhmtkbr.domain.repository.StudentStudiesRepository;
import com.ilhamrhmtkbr.domain.repository.StudentTransactionRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ProfileViewModel extends ViewModel {
    private final StudentCartRepository cartRepository;
    private final StudentCertificateRepository certificateRepository;
    private final StudentStudiesRepository studiesRepository;
    private final StudentQuestionRepository questionRepository;
    private final StudentReviewRepository reviewRepository;
    private final StudentTransactionRepository transactionRepository;

    @Inject
    public ProfileViewModel(StudentCartRepository cartRepository, StudentCertificateRepository certificateRepository, StudentStudiesRepository studiesRepository, StudentQuestionRepository questionRepository, StudentReviewRepository reviewRepository, StudentTransactionRepository transactionRepository) {
        this.cartRepository = cartRepository;
        this.certificateRepository = certificateRepository;
        this.studiesRepository = studiesRepository;
        this.questionRepository = questionRepository;
        this.reviewRepository = reviewRepository;
        this.transactionRepository = transactionRepository;
    }

    public LiveData<Resource<List<Cart>>> getCarts() {
        return cartRepository.getAllCarts();
    }

    public LiveData<Resource<List<Certificate>>> getCertificates() {
        return certificateRepository.getAllCertificates();
    }

    public LiveData<Resource<List<Course>>> getCourses() {
        return studiesRepository.getAllCourses();
    }

    public LiveData<Resource<List<Question>>> getQuestions() {
        return questionRepository.getAllQuestions();
    }

    public LiveData<Resource<List<Review>>> getReviews() {
        return reviewRepository.getReviews();
    }

    public LiveData<Resource<List<Transaction>>> getTransactions() {
        return transactionRepository.getAllTransactions();
    }

    public void refreshCarts() {
        cartRepository.fetch("1", "desc");
    }

    public void refreshCertificates() {
        certificateRepository.fetch("1", "desc");
    }

    public void refreshCourses() {
        studiesRepository.courses("1", "desc");
    }

    public void refreshQuestions() {
        questionRepository.fetch("1", "desc");
    }

    public void refreshReviews() {
        reviewRepository.fetch("1", "desc");
    }

    public void refreshTransactions() {
        transactionRepository.fetch("1", "desc", "all");
    }
}
