package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructor_lessons")
public class InstructorLessonsEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String section_id;
    public String title;
    public String description;
    public String code;
    public String created_at;
    public String order_in_section;
}
