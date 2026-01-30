package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentCartsPaginationEntity;

import java.util.List;

@Dao
public interface StudentCartsPagination {
    @Query("Select * FROM student_carts_pagination")
    LiveData<List<StudentCartsPaginationEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentCartsPaginationEntity> cartsPagination);

    @Query("DELETE FROM student_carts_pagination")
    void clearAll();

    @Transaction
    default void refresh(List<StudentCartsPaginationEntity> cartsPagination){
        clearAll();
        insertAll(cartsPagination);
    }
}
