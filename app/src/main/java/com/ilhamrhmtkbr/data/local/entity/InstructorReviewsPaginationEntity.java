package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructor_reviews_pagination")
public class InstructorReviewsPaginationEntity {
    @PrimaryKey
    @NonNull
    public String label;
    public String url;
    public boolean isActive;
}
