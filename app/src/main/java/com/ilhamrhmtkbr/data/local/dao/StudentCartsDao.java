package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentCartsEntity;

import java.util.List;

@Dao
public interface StudentCartsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentCartsEntity> carts);

    @Query("SELECT * FROM student_carts")
    LiveData<List<StudentCartsEntity>> getAll();

    @Query("DELETE FROM student_carts")
    void clearAll();

    @Transaction
    default void refresh(List<StudentCartsEntity> carts) {
        clearAll();
        insertAll(carts);
    }
}
