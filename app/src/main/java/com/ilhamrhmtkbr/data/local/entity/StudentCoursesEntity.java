package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_courses")
public class StudentCoursesEntity {
    @PrimaryKey
    @NonNull
    public String id;

    public String title;
    public String description;
    public String image;
    public String created_at;
}
