package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_transactions_pagination")
public class StudentTransactionsPaginationEntity {
    @PrimaryKey
    @NonNull
    public String label;
    public String url;
    public Boolean isActive;

}
