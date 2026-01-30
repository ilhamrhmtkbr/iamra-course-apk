package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentCertificatesEntity;

import java.util.List;

@Dao
public interface StudentCertificatesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentCertificatesEntity> certificates);

    @Query("SELECT * FROM student_certificates")
    LiveData<List<StudentCertificatesEntity>> getAll();

    @Query("DELETE FROM student_certificates")
    void clearAll();

    @Transaction
    default void refresh(List<StudentCertificatesEntity> certificates) {
        clearAll();
        insertAll(certificates);
    }
}
