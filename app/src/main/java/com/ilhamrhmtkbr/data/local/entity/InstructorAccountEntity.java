package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructor_account")
public class InstructorAccountEntity {
    @PrimaryKey
    @NonNull
    public String account_id;
    public String bank_name;
    public String alias_name;
}
