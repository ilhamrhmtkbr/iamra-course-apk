package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "instructor_answers")
public class InstructorAnswersEntity {
    @PrimaryKey
    @NonNull
    public String question_id;

    public String course_title;
    public String question;
    public String question_created_at;

    @Nullable
    public String answer;
    @Nullable
    public String answer_id;

    public String student_name;
}
