package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentTransactionsEntity;

import java.util.List;

@Dao
public interface StudentTransactionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentTransactionsEntity> data);

    @Query("SELECT * FROM student_transactions")
    LiveData<List<StudentTransactionsEntity>> getAll();

    @Query("DELETE FROM student_transactions")
    void clearAll();

    @Transaction
    default void refresh(List<StudentTransactionsEntity> data) {
        clearAll();
        insertAll(data);
    };
}
