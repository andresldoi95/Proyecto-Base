package com.techtraining.cosechasapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.techtraining.cosechasapp.Helper;

@Database(entities = {User.class, Empresa.class}, version = Helper.DB_VERSION)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract EmpresaDao empresaDao();
}
