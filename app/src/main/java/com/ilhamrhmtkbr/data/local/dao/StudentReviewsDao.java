package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentReviewsEntity;

import java.util.List;

@Dao
public interface StudentReviewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentReviewsEntity> data);

    @Query("SELECT * FROM student_reviews")
    LiveData<List<StudentReviewsEntity>> getAll();

    @Query("DELETE FROM student_reviews")
    void clearAll();

    @Transaction
    default void refresh(List<StudentReviewsEntity> data) {
        clearAll();
        insertAll(data);
    }
}
