package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructor_courses_likes")
public class InstructorCoursesLikesEntity {
    @PrimaryKey
    @NonNull
    public String username;
    public String full_name;
}
