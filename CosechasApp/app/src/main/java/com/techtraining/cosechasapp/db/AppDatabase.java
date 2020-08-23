package com.techtraining.cosechasapp.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.techtraining.cosechasapp.Helper;

@Database(entities = {User.class, Empresa.class, Camion.class, Controlador.class}, version = Helper.DB_VERSION)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract EmpresaDao empresaDao();
    public abstract CamionDao camionDao();
    public abstract ControladorDao controladorDao();
}
