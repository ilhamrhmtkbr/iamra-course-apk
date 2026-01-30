package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructor_socials")
public class InstructorSocialsEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String url_link;
    public String app_name;
    public String display_name;
}
