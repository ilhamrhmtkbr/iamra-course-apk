package com.ilhamrhmtkbr.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructor_reviews")
public class InstructorReviewsEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String course_title;
    public String student_full_name;
    public String student_review;
    public String student_rating;
    public String created_at;
}
