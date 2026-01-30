package com.ilhamrhmtkbr.di;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

import com.ilhamrhmtkbr.data.repository.AuthRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.ForumRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.InstructorAccountRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.InstructorAnswersRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.InstructorCouponsRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.InstructorCoursesRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.InstructorEarningsRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.InstructorLessonsRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.InstructorReviewsRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.InstructorSectionsRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.InstructorSocialsRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.MemberRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.PublicRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.StudentCartRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.StudentCertificateRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.StudentProgressesRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.StudentQuestionRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.StudentReviewRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.StudentStudiesRepositoryImpl;
import com.ilhamrhmtkbr.data.repository.StudentTransactionRepositoryImpl;
import com.ilhamrhmtkbr.domain.repository.AuthRepository;
import com.ilhamrhmtkbr.domain.repository.ForumRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorAccountRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorAnswersRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorCouponsRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorCoursesRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorEarningsRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorLessonsRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorReviewsRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorSectionsRepository;
import com.ilhamrhmtkbr.domain.repository.InstructorSocialsRepository;
import com.ilhamrhmtkbr.domain.repository.MemberRepository;
import com.ilhamrhmtkbr.domain.repository.PublicRepository;
import com.ilhamrhmtkbr.domain.repository.StudentCartRepository;
import com.ilhamrhmtkbr.domain.repository.StudentCertificateRepository;
import com.ilhamrhmtkbr.domain.repository.StudentProgressesRepository;
import com.ilhamrhmtkbr.domain.repository.StudentQuestionRepository;
import com.ilhamrhmtkbr.domain.repository.StudentReviewRepository;
import com.ilhamrhmtkbr.domain.repository.StudentStudiesRepository;
import com.ilhamrhmtkbr.domain.repository.StudentTransactionRepository;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    public abstract AuthRepository bindAuthRepository(AuthRepositoryImpl impl);

    @Binds
    public abstract ForumRepository bindForumRepository(ForumRepositoryImpl impl);

    @Binds
    public abstract InstructorAccountRepository bindInstructorAccountRepository(
            InstructorAccountRepositoryImpl impl);

    @Binds
    public abstract InstructorAnswersRepository bindInstructorAnswersRepository(
            InstructorAnswersRepositoryImpl impl);

    @Binds
    public abstract InstructorCouponsRepository bindInstructorCouponsRepository(
            InstructorCouponsRepositoryImpl impl);

    @Binds
    public abstract InstructorCoursesRepository bindInstructorCoursesRepository(
            InstructorCoursesRepositoryImpl impl);

    @Binds
    public abstract InstructorEarningsRepository bindInstructorEarningsRepository(
            InstructorEarningsRepositoryImpl impl);

    @Binds
    public abstract InstructorLessonsRepository bindInstructorLessonsRepository(
            InstructorLessonsRepositoryImpl impl);

    @Binds
    public abstract InstructorReviewsRepository bindInstructorReviewsRepository(
            InstructorReviewsRepositoryImpl impl);

    @Binds
    public abstract InstructorSectionsRepository bindInstructorSectionsRepository(
            InstructorSectionsRepositoryImpl impl);

    @Binds
    public abstract InstructorSocialsRepository bindInstructorSocialsRepository(
            InstructorSocialsRepositoryImpl impl);


    @Binds
    public abstract MemberRepository bindMemberRepository(
            MemberRepositoryImpl impl);

    @Binds
    public abstract PublicRepository bindPublicRepository(
            PublicRepositoryImpl impl);

    @Binds
    public abstract StudentCartRepository bindStudentCartRepository(
            StudentCartRepositoryImpl impl);

    @Binds
    public abstract StudentCertificateRepository bindStudentCertificateRepository(
            StudentCertificateRepositoryImpl impl);

    @Binds
    public abstract StudentProgressesRepository bindStudentProgressesRepository(
            StudentProgressesRepositoryImpl impl);

    @Binds
    public abstract StudentQuestionRepository bindStudentQuestionRepository(
            StudentQuestionRepositoryImpl impl);

    @Binds
    public abstract StudentReviewRepository bindStudentReviewRepository(
            StudentReviewRepositoryImpl impl);

    @Binds
    public abstract StudentStudiesRepository bindStudentStudiesRepository(
            StudentStudiesRepositoryImpl impl);

    @Binds
    public abstract StudentTransactionRepository bindStudentTransactionRepository(
            StudentTransactionRepositoryImpl impl);
}
