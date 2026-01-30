package com.ilhamrhmtkbr.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.ilhamrhmtkbr.data.local.entity.InstructorSectionsEntity;

import java.util.List;

@Dao
public interface InstructorSectionsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstructorSectionsEntity> data);

    @Query("SELECT * FROM instructor_sections")
    LiveData<List<InstructorSectionsEntity>> getAll();

    @Query("DELETE FROM instructor_sections")
    void clearAll();

    @Transaction
    default void refresh(List<InstructorSectionsEntity> data) {
        clearAll();
        insertAll(data);
    }
}
