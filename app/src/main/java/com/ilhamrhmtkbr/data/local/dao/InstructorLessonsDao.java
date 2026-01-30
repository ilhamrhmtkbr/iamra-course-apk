package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorLessonsEntity;

import java.util.List;

@Dao
public interface InstructorLessonsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorLessonsEntity> data);

    @Query("SELECT * FROM instructor_lessons")
    LiveData<List<InstructorLessonsEntity>> getAll();

    @Query("DELETE FROM instructor_lessons")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorLessonsEntity> data) {
        clearAll();
        insertAll(data);
    }
}
