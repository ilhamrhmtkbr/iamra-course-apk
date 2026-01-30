package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorLessonsPaginationEntity;

import java.util.List;

@Dao
public interface InstructorLessonsPagination {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorLessonsPaginationEntity> data);

    @Query("SELECT * FROM instructor_lessons_pagination")
    LiveData<List<InstructorLessonsPaginationEntity>> getAll();

    @Query("DELETE FROM instructor_lessons_pagination")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorLessonsPaginationEntity> data){
        clearAll();
        insertAll(data);
    }
}
