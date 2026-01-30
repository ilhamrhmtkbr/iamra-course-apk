package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_progresses_pagination")
public class StudentProgressesPaginationEntity {
    @PrimaryKey
    @NonNull
    public String label;

    public String url;
    public Boolean isActive;
}
