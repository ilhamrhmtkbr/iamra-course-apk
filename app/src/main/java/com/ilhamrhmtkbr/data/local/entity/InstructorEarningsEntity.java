package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructor_earnings")
public class InstructorEarningsEntity {
    @PrimaryKey
    @NonNull
    public String order_id;
    public String instructor_course;
    public String student_full_name;
    public String amount;
    public String status;
    public String created_at;
}
