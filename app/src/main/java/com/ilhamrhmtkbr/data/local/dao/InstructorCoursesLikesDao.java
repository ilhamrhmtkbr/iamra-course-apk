package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorCoursesLikesEntity;

import java.util.List;

@Dao
public interface InstructorCoursesLikesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorCoursesLikesEntity> data);

    @Query("SELECT * FROM instructor_courses_likes")
    LiveData<List<InstructorCoursesLikesEntity>> getAll();

    @Query("DELETE FROM instructor_courses_likes")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorCoursesLikesEntity> data) {
        clearAll();
        insertAll(data);
    }
}
