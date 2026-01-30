package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_reviews")
public class StudentReviewsEntity {
    @PrimaryKey
    @NonNull
    public String id;

    public String review;
    public String rating;
    public String created_at;
    public String course_id;
    public String course_title;
    public String course_desc;
}
