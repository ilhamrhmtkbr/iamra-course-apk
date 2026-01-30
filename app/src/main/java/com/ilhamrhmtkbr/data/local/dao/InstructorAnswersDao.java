package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorAnswersEntity;

import java.util.List;

@Dao
public interface InstructorAnswersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorAnswersEntity> data);

    @Query("SELECT * FROM instructor_answers")
    LiveData<List<InstructorAnswersEntity>> getAll();

    @Query("DELETE FROM instructor_answers")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorAnswersEntity> data) {
        clearAll();
        insertAll(data);
    }
}
