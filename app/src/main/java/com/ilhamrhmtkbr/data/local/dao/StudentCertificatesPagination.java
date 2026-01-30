package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentCertificatesPaginationEntity;

import java.util.List;

@Dao
public interface StudentCertificatesPagination {
    @Query("Select * FROM student_certificates_pagination")
    LiveData<List<StudentCertificatesPaginationEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentCertificatesPaginationEntity> certificatesPaginationEntities);

    @Query("DELETE FROM student_certificates_pagination")
    void clearAll();

    @Transaction
    default void refresh(List<StudentCertificatesPaginationEntity> certificatesPaginationEntities){
        clearAll();
        insertAll(certificatesPaginationEntities);
    }
}
