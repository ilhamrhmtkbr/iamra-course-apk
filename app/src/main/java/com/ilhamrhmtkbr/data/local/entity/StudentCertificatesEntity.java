package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_certificates")
public class StudentCertificatesEntity {
    @PrimaryKey
    @NonNull
    public String id;

    public String student_name;
    public String course_title;
    public String created_at;
}
