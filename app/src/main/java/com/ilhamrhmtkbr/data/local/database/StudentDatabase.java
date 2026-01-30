package com.ilhamrhmtkbr.data.local.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

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
import com.ilhamrhmtkbr.data.local.entity.StudentCartsEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentCartsPaginationEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentCertificatesEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentCertificatesPaginationEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentCoursesEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentCoursesPaginationEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentProgressesEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentProgressesPaginationEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentQuestionsEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentQuestionsPaginationEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentReviewsEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentReviewsPaginationEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentTransactionsEntity;
import com.ilhamrhmtkbr.data.local.entity.StudentTransactionsPaginationEntity;

@Database(entities = {
        StudentCartsEntity.class,
        StudentCartsPaginationEntity.class,
        StudentCertificatesEntity.class,
        StudentCertificatesPaginationEntity.class,
        StudentCoursesEntity.class,
        StudentCoursesPaginationEntity.class,
        StudentProgressesEntity.class,
        StudentProgressesPaginationEntity.class,
        StudentQuestionsEntity.class,
        StudentQuestionsPaginationEntity.class,
        StudentReviewsEntity.class,
        StudentReviewsPaginationEntity.class,
        StudentTransactionsEntity.class,
        StudentTransactionsPaginationEntity.class,
}, version = 10, exportSchema = false) // version harus diubah tiap kali tambah entities atau ubah database , kenapa ? Karena Room butuh tahu kapan struktur database berubah.
public abstract class StudentDatabase extends RoomDatabase {
    public abstract StudentCartsDao carstDao();
    public abstract StudentCartsPagination cartsPagination();

    public abstract StudentCertificatesDao certificatesDao();
    public abstract StudentCertificatesPagination certificatesPagination();

    public abstract StudentCoursesDao coursesDao();
    public abstract StudentCoursesPagination coursesPagination();

    public abstract StudentProgressesDao progressesDao();
    public abstract StudentProgressesPagination progressesPagination();

    public abstract StudentQuestionsDao questionsDao();
    public abstract StudentQuestionsPagination questionsPagination();

    public abstract StudentReviewsDao reviewsDao();
    public abstract StudentReviewsPagination reviewsPagination();

    public abstract StudentTransactionsDao transactionsDao();
    public abstract StudentTransactionsPagination transactionsPagination();
}
