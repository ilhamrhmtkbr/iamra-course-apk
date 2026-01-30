package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructor_courses")
public class InstructorCoursesEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String image;
    public String description;
    public String editor;
}
