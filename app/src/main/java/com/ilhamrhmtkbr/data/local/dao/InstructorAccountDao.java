package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorAccountEntity;

import java.util.List;

@Dao
public interface InstructorAccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorAccountEntity> data);

    @Query("SELECT * FROM instructor_account")
    LiveData<List<InstructorAccountEntity>> getAll();

    @Query("DELETE FROM instructor_account")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorAccountEntity> data) {
        clearAll();
        insertAll(data);
    }
}
