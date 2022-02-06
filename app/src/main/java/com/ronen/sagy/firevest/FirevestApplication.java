package com.ronen.sagy.firevest;

import android.app.Application;

import androidx.room.Room;

import com.ronen.sagy.firevest.services.model.AppDatabase;

public class FirevestApplication extends Application {

    private static AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        database = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-cache").build();
    }

    public static AppDatabase getDatabase() {
        return database;
    }
}