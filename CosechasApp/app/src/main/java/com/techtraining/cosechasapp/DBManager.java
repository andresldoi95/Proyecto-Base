package com.techtraining.cosechasapp;

import android.content.Context;

import androidx.room.Room;

import com.techtraining.cosechasapp.db.AppDatabase;

public class DBManager {
    private static AppDatabase instance;
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, Helper.DB_NAME).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
