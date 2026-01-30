package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentProgressesPaginationEntity;

import java.util.List;

@Dao
public interface StudentProgressesPagination {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentProgressesPaginationEntity> data);

    @Query("SELECT * FROM student_progresses_pagination")
    LiveData<List<StudentProgressesPaginationEntity>> getAll();

    @Query("DELETE FROM student_progresses_pagination")
    void clearAll();

    @Transaction
    default void refresh(List<StudentProgressesPaginationEntity> data) {
        clearAll();
        insertAll(data);
    }
}
