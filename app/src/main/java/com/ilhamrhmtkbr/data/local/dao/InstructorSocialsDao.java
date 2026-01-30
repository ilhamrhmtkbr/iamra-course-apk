package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorSocialsEntity;

import java.util.List;

@Dao
public interface InstructorSocialsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorSocialsEntity> data);

    @Query("SELECT * FROM instructor_socials")
    LiveData<List<InstructorSocialsEntity>> getAll();

    @Query("DELETE FROM instructor_socials")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorSocialsEntity> data) {
        clearAll();
        insertAll(data);
    }
}
