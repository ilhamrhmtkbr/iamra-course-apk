package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorAnswersPaginationEntity;

import java.util.List;

@Dao
public interface InstructorAnswersPagination {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorAnswersPaginationEntity> data);

    @Query("SELECT * FROM instructor_answers_pagination")
    LiveData<List<InstructorAnswersPaginationEntity>> getAll();

    @Query("DELETE FROM instructor_answers_pagination")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorAnswersPaginationEntity> data){
        clearAll();
        insertAll(data);
    }
}
