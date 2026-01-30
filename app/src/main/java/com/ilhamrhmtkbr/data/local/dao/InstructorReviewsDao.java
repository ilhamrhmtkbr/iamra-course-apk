package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorReviewsEntity;

import java.util.List;

@Dao
public interface InstructorReviewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorReviewsEntity> data);

    @Query("SELECT * FROM instructor_reviews")
    LiveData<List<InstructorReviewsEntity>> getAll();

    @Query("DELETE FROM instructor_reviews")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorReviewsEntity> data) {
        clearAll();
        insertAll(data);
    }
}
