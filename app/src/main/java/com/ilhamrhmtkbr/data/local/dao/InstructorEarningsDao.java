package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorEarningsEntity;

import java.util.List;

@Dao
public interface InstructorEarningsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorEarningsEntity> data);

    @Query("SELECT * FROM instructor_earnings")
    LiveData<List<InstructorEarningsEntity>> getAll();

    @Query("DELETE FROM instructor_earnings")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorEarningsEntity> data) {
        clearAll();
        insertAll(data);
    }
}
