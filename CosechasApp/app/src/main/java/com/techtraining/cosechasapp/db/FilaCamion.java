package com.techtraining.cosechasapp.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FilaCamion {
    @NonNull
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "filas")
    public int filas;

    @ColumnInfo(name = "columnas")
    public int columnas;

    @ColumnInfo(name = "camion_id")
    public int camionId;
}
