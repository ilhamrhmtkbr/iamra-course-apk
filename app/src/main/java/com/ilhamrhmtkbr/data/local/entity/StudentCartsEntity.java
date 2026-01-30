package com.ilhamrhmtkbr.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "student_carts")
public class StudentCartsEntity {
    @PrimaryKey
    @NonNull
    public String id;

    public String instructorCourseId;

    @Embedded(prefix = "course_")
    public InstructorCourse instructorCourse;

    public static class InstructorCourse {
        public String id;
        public String title;
        public String description;
        public String image;
        public String price;
        public String level;
        public String status;
    }
}
