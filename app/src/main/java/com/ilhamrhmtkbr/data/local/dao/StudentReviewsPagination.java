package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentReviewsPaginationEntity;

import java.util.List;

@Dao
public interface StudentReviewsPagination {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentReviewsPaginationEntity> data);

    @Query("SELECT * FROM student_reviews_pagination")
    LiveData<List<StudentReviewsPaginationEntity>> getAll();

    @Query("DELETE FROM student_reviews_pagination")
    void clearAll();

    @Transaction
    default void refresh(List<StudentReviewsPaginationEntity> data) {
        clearAll();
        insertAll(data);
    }
}
