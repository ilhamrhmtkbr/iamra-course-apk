package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorReviewsPaginationEntity;

import java.util.List;

@Dao
public interface InstructorReviewsPagination {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorReviewsPaginationEntity> data);

    @Query("SELECT * FROM instructor_reviews_pagination")
    LiveData<List<InstructorReviewsPaginationEntity>> getAll();

    @Query("DELETE FROM instructor_reviews_pagination")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorReviewsPaginationEntity> data){
        clearAll();
        insertAll(data);
    }
}
