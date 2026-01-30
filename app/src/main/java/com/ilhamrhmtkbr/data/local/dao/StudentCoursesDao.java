package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentCoursesEntity;

import java.util.List;

@Dao
public interface StudentCoursesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentCoursesEntity> data);

    @Query("SELECT * FROM student_courses")
    LiveData<List<StudentCoursesEntity>> getAll();

    @Query("DELETE FROM student_courses")
    void clearAll();

    @Transaction
    default void refresh(List<StudentCoursesEntity> data) {
        clearAll();
        insertAll(data);
    }
}
