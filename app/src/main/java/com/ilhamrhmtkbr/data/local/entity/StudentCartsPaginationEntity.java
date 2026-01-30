package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_carts_pagination")
public class StudentCartsPaginationEntity {
    @PrimaryKey
    @NonNull
    public String label;
    public String url;
    public boolean isActive;
}
