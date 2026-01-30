package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentCoursesPaginationEntity;

import java.util.List;

@Dao
public interface StudentCoursesPagination {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentCoursesPaginationEntity> data);

    @Query("SELECT * FROM student_courses_pagination")
    LiveData<List<StudentCoursesPaginationEntity>> getAll();

    @Query("DELETE FROM student_courses_pagination")
    void clearAll();

    @Transaction
    default void refresh(List<StudentCoursesPaginationEntity> data) {
        clearAll();
        insertAll(data);
    }
}
