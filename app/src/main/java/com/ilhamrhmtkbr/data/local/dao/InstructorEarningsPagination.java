package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorEarningsPaginationEntity;

import java.util.List;

@Dao
public interface InstructorEarningsPagination {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorEarningsPaginationEntity> data);

    @Query("SELECT * FROM instructor_earnings_pagination")
    LiveData<List<InstructorEarningsPaginationEntity>> getAll();

    @Query("DELETE FROM instructor_earnings_pagination")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorEarningsPaginationEntity> data){
        clearAll();
        insertAll(data);
    }
}
