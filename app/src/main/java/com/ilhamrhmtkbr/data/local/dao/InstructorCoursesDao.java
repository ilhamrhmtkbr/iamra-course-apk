package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorCoursesEntity;

import java.util.List;

@Dao
public interface InstructorCoursesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorCoursesEntity> data);

    @Query("SELECT * FROM instructor_courses")
    LiveData<List<InstructorCoursesEntity>> getAll();

    @Query("DELETE FROM instructor_courses")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorCoursesEntity> data) {
        clearAll();
        insertAll(data);
    }
}
