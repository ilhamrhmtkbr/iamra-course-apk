package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructor_coupons")
public class InstructorCouponsEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String discount;
    public String max_redemptions;
    public String expiry_date;
}
