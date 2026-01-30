package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorCouponsPaginationEntity;

import java.util.List;

@Dao
public interface InstructorCouponsPagination {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorCouponsPaginationEntity> data);

    @Query("SELECT * FROM instructor_coupons_pagination")
    LiveData<List<InstructorCouponsPaginationEntity>> getAll();

    @Query("DELETE FROM instructor_coupons_pagination")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorCouponsPaginationEntity> data){
        clearAll();
        insertAll(data);
    }
}
