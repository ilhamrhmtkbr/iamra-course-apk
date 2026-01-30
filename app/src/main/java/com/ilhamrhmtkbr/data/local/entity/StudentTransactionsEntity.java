package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_transactions")
public class StudentTransactionsEntity {
    @PrimaryKey
    @NonNull
    public String order_id;
    public String amount;
    public String midtrans_data;
    public String status;
    public String created_at;
}
