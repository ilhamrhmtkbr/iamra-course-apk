package com.ilhamrhmtkbr.data.local.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

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
import com.ilhamrhmtkbr.data.local.entity.InstructorAccountEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorAnswersEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorAnswersPaginationEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorCouponsEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorCouponsPaginationEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorCoursesEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorCoursesLikesEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorCoursesPaginationEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorEarningsEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorEarningsPaginationEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorLessonsEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorLessonsPaginationEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorReviewsEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorReviewsPaginationEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorSectionsEntity;
import com.ilhamrhmtkbr.data.local.entity.InstructorSocialsEntity;

@Database(entities = {
        InstructorAccountEntity.class,
        InstructorAnswersEntity.class,
        InstructorAnswersPaginationEntity.class,
        InstructorCouponsEntity.class,
        InstructorCouponsPaginationEntity.class,
        InstructorCoursesEntity.class,
        InstructorCoursesPaginationEntity.class,
        InstructorCoursesLikesEntity.class,
        InstructorEarningsEntity.class,
        InstructorEarningsPaginationEntity.class,
        InstructorLessonsEntity.class,
        InstructorLessonsPaginationEntity.class,
        InstructorReviewsEntity.class,
        InstructorReviewsPaginationEntity.class,
        InstructorSectionsEntity.class,
        InstructorSocialsEntity.class
}, version = 11, exportSchema = false)
public abstract class InstructorDatabase extends RoomDatabase {
    public abstract InstructorAccountDao accountDao();

    public abstract InstructorAnswersDao answersDao();
    public abstract InstructorAnswersPagination answersPagination();

    public abstract InstructorCouponsDao couponsDao();
    public abstract InstructorCouponsPagination couponsPagination();

    public abstract InstructorCoursesDao coursesDao();
    public abstract InstructorCoursesPagination coursesPagination();
    public abstract InstructorCoursesLikesDao coursesLikesDao();

    public abstract InstructorEarningsDao earningsDao();
    public abstract InstructorEarningsPagination earningsPagination();

    public abstract InstructorLessonsDao lessonsDao();
    public abstract InstructorLessonsPagination lessonsPagination();

    public abstract InstructorReviewsDao reviewsDao();
    public abstract InstructorReviewsPagination reviewsPagination();

    public abstract InstructorSectionsDao sectionsDao();

    public abstract InstructorSocialsDao socialsDao();
}
