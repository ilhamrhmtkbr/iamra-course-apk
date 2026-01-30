package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_questions")
public class StudentQuestionsEntity {
    @PrimaryKey
    @NonNull
    public String id;

    public String instructor_course_id;
    public String question;
    public String created_at;
    public String course_title;
}
