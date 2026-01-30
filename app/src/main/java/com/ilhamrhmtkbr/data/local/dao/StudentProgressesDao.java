package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentProgressesEntity;

import java.util.List;

@Dao
public interface StudentProgressesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentProgressesEntity> data);

    @Query("SELECT * FROM student_progresses")
    LiveData<List<StudentProgressesEntity>> getAllProgresses();

    @Query("DELETE FROM student_progresses")
    void clearAll();

    @Transaction
    default void refresh(List<StudentProgressesEntity> data){
        clearAll();
        insertAll(data);
    }
}
