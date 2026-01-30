package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructor_sections")
public class InstructorSectionsEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String title;
    public String order_in_course;
}
