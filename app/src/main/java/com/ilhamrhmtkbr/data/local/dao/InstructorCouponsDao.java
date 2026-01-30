package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorCouponsEntity;

import java.util.List;

@Dao
public interface InstructorCouponsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorCouponsEntity> data);

    @Query("SELECT * FROM instructor_coupons")
    LiveData<List<InstructorCouponsEntity>> getAll();

    @Query("DELETE FROM instructor_coupons")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorCouponsEntity> data) {
        clearAll();
        insertAll(data);
    }
}
