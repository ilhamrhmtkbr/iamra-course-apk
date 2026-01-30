package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructor_lessons_pagination")
public class InstructorLessonsPaginationEntity {
    @PrimaryKey
    @NonNull
    public String label;
    public String url;
    public boolean isActive;
}
