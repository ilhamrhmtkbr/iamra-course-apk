package com.ilhamrhmtkbr.di;

import android.content.Context;

import com.ilhamrhmtkbr.data.local.dao.InstructorAccountDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorAnswersDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorAnswersPagination;
import com.ilhamrhmtkbr.data.local.dao.InstructorCouponsDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorCouponsPagination;
import com.ilhamrhmtkbr.data.local.dao.InstructorCoursesDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorCoursesLikesDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorCoursesPagination;
import com.ilhamrhmtkbr.data.local.dao.InstructorEarningsDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorEarningsPagination;
import com.ilhamrhmtkbr.data.local.dao.InstructorLessonsDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorLessonsPagination;
import com.ilhamrhmtkbr.data.local.dao.InstructorReviewsDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorReviewsPagination;
import com.ilhamrhmtkbr.data.local.dao.InstructorSectionsDao;
import com.ilhamrhmtkbr.data.local.dao.InstructorSocialsDao;
import com.ilhamrhmtkbr.data.local.dao.StudentCartsDao;
import com.ilhamrhmtkbr.data.local.dao.StudentCartsPagination;
import com.ilhamrhmtkbr.data.local.dao.StudentCertificatesDao;
import com.ilhamrhmtkbr.data.local.dao.StudentCertificatesPagination;
import com.ilhamrhmtkbr.data.local.dao.StudentCoursesDao;
import com.ilhamrhmtkbr.data.local.dao.StudentCoursesPagination;
import com.ilhamrhmtkbr.data.local.dao.StudentProgressesDao;
import com.ilhamrhmtkbr.data.local.dao.StudentProgressesPagination;
import com.ilhamrhmtkbr.data.local.dao.StudentQuestionsDao;
import com.ilhamrhmtkbr.data.local.dao.StudentQuestionsPagination;
import com.ilhamrhmtkbr.data.local.dao.StudentReviewsDao;
import com.ilhamrhmtkbr.data.local.dao.StudentReviewsPagination;
import com.ilhamrhmtkbr.data.local.dao.StudentTransactionsDao;
import com.ilhamrhmtkbr.data.local.dao.StudentTransactionsPagination;
import com.ilhamrhmtkbr.data.local.database.AppDatabaseSingleton;
import com.ilhamrhmtkbr.data.local.database.InstructorDatabase;
import com.ilhamrhmtkbr.data.local.database.StudentDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public InstructorDatabase provideDatabaseInstructor(
            @ApplicationContext Context context
    ) {
        return AppDatabaseSingleton.getInstructorDatabase(context);
    }

    @Provides
    public InstructorAccountDao provideInstructorAccountDao(InstructorDatabase db) {
        return db.accountDao();
    }

    @Provides
    public InstructorAnswersDao provideInstructorAnswersDao(InstructorDatabase db) {
        return db.answersDao();
    }

    @Provides
    public InstructorAnswersPagination provideInstructorAnswersPagination(InstructorDatabase db) {
        return db.answersPagination();
    }

    @Provides
    public InstructorCouponsDao provideInstructorCouponsDao(InstructorDatabase db) {
        return db.couponsDao();
    }

    @Provides
    public InstructorCouponsPagination provideInstructorCouponsPagination(InstructorDatabase db) {
        return db.couponsPagination();
    }

    @Provides
    public InstructorCoursesDao provideInstructorCoursesDao(InstructorDatabase db) {
        return db.coursesDao();
    }

    @Provides
    public InstructorCoursesLikesDao provideInstructorCoursesLikesDao(InstructorDatabase db) {
        return db.coursesLikesDao();
    }

    @Provides
    public InstructorCoursesPagination provideInstructorCoursesPagination(InstructorDatabase db) {
        return db.coursesPagination();
    }

    @Provides
    public InstructorEarningsDao provideInstructorEarningsDao(InstructorDatabase db) {
        return db.earningsDao();
    }

    @Provides
    public InstructorEarningsPagination provideInstructorEarningsPagination(InstructorDatabase db) {
        return db.earningsPagination();
    }

    @Provides
    public InstructorLessonsDao provideInstructorLessonsDao(InstructorDatabase db) {
        return db.lessonsDao();
    }

    @Provides
    public InstructorLessonsPagination provideInstructorLessonsPagination(InstructorDatabase db) {
        return db.lessonsPagination();
    }

    @Provides
    public InstructorReviewsDao provideInstructorReviewsDao(InstructorDatabase db) {
        return db.reviewsDao();
    }

    @Provides
    public InstructorReviewsPagination provideInstructorReviewsPagination(InstructorDatabase db) {
        return db.reviewsPagination();
    }

    @Provides
    public InstructorSectionsDao provideInstructorSectionsDao(InstructorDatabase db) {
        return db.sectionsDao();
    }

    @Provides
    public InstructorSocialsDao provideInstructorSocialsDao(InstructorDatabase db) {
        return db.socialsDao();
    }

    @Provides
    @Singleton
    public StudentDatabase provideDatabaseStudent(
            @ApplicationContext Context context
    ) {
        return AppDatabaseSingleton.getStudentDatabase(context);
    }

    @Provides
    public StudentCartsDao provideStudentCartsDao(StudentDatabase db) {
        return db.carstDao();
    }

    @Provides
    public StudentCartsPagination provideStudentCartsPagination(StudentDatabase db) {
        return db.cartsPagination();
    }

    @Provides
    public StudentCertificatesDao provideStudentCertificatesDao(StudentDatabase db) {
        return db.certificatesDao();
    }

    @Provides
    public StudentCertificatesPagination provideStudentCertificatesPagination(StudentDatabase db) {
        return db.certificatesPagination();
    }

    @Provides
    public StudentCoursesDao provideStudentCoursesDao(StudentDatabase db) {
        return db.coursesDao();
    }

    @Provides
    public StudentCoursesPagination provideStudentCoursesPagination(StudentDatabase db) {
        return db.coursesPagination();
    }

    @Provides
    public StudentProgressesDao provideStudentProgressesDao(StudentDatabase db) {
        return db.progressesDao();
    }

    @Provides
    public StudentProgressesPagination provideStudentProgressesPagination(StudentDatabase db) {
        return db.progressesPagination();
    }

    @Provides
    public StudentQuestionsDao provideStudentQuestionsDao(StudentDatabase db) {
        return db.questionsDao();
    }

    @Provides
    public StudentQuestionsPagination provideStudentQuestionsPagination(StudentDatabase db) {
        return db.questionsPagination();
    }

    @Provides
    public StudentReviewsDao provideStudentReviewsDao(StudentDatabase db) {
        return db.reviewsDao();
    }

    @Provides
    public StudentReviewsPagination provideStudentReviewsPagination(StudentDatabase db) {
        return db.reviewsPagination();
    }

    @Provides
    public StudentTransactionsDao provideStudentTransactionsDao(StudentDatabase db) {
        return db.transactionsDao();
    }

    @Provides
    public StudentTransactionsPagination provideStudentTransactionsPagination(StudentDatabase db) {
        return db.transactionsPagination();
    }
}

