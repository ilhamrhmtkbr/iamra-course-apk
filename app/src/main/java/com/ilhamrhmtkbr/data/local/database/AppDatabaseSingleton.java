package com.ilhamrhmtkbr.data.local.database;

import android.content.Context;

import androidx.room.Room;

public class AppDatabaseSingleton {
    private static StudentDatabase studentInstance;
    private static InstructorDatabase instructorInstance;

    public static synchronized StudentDatabase getStudentDatabase(Context context) {
        if (studentInstance == null) {
            studentInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            StudentDatabase.class,
                            "student"
                    )
                    .fallbackToDestructiveMigration() // agar Room menghapus dan membuat ulang DB saat versi berubah (untuk development)
                    .build();
        }
        return studentInstance;
    }

    public static synchronized InstructorDatabase getInstructorDatabase(Context context) {
        if (instructorInstance == null) {
            instructorInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            InstructorDatabase.class,
                            "instructor"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instructorInstance;
    }
}
