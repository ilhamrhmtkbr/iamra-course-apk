package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_progresses")
public class StudentProgressesEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String title;

    public String completed_sections;
    public String total_sections;
}
