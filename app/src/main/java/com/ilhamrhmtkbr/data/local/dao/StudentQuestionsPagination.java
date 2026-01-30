package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.StudentQuestionsPaginationEntity;

import java.util.List;

@Dao
public interface StudentQuestionsPagination {
    @Query("Select * FROM student_questions_pagination")
    LiveData<List<StudentQuestionsPaginationEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<StudentQuestionsPaginationEntity> certificatesPaginationEntities);

    @Query("DELETE FROM student_questions_pagination")
    void clearAll();

    @Transaction
    default void refreshAll(List<StudentQuestionsPaginationEntity> certificatesPaginationEntities){
        clearAll();
        insertAll(certificatesPaginationEntities);
    }
}
