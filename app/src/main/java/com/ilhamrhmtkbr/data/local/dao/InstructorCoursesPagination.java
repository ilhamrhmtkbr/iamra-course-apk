package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorCoursesPaginationEntity;

import java.util.List;

@Dao
public interface InstructorCoursesPagination {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorCoursesPaginationEntity> data);

    @Query("SELECT * FROM instructor_courses_pagination")
    LiveData<List<InstructorCoursesPaginationEntity>> getAll();

    @Query("DELETE FROM instructor_courses_pagination")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorCoursesPaginationEntity> data){
        clearAll();
        insertAll(data);
    }
}
