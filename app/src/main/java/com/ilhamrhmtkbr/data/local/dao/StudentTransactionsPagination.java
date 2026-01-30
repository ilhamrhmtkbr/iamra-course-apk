package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentTransactionsPaginationEntity;

import java.util.List;

@Dao
public interface StudentTransactionsPagination {
    @Query("SELECT * FROM student_transactions_pagination")
    LiveData<List<StudentTransactionsPaginationEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentTransactionsPaginationEntity> data);

    @Query("DELETE FROM student_transactions_pagination")
    void clearAll();

    @Transaction
    default void refresh(List<StudentTransactionsPaginationEntity> data) {
        clearAll();
        insertAll(data);
    }
}
