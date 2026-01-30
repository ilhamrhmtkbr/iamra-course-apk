package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentQuestionsEntity;

import java.util.List;

@Dao
public interface StudentQuestionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentQuestionsEntity> data);

    @Query("SELECT * FROM student_questions")
    LiveData<List<StudentQuestionsEntity>> getAll();

    @Query("DELETE FROM student_questions")
    void clearAll();

    @Transaction
    default void refresh(List<StudentQuestionsEntity> data) {
        clearAll();
        insertAll(data);
    }
}
