package com.techtraining.cosechasapp.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FilaCosecha {
    @NonNull
    @PrimaryKey
    public String id;
    @ColumnInfo(name = "cosecha_id")
    public String cosechaId;

    @ColumnInfo(name = "indice")
    public int indice;

    @ColumnInfo(name = "bft")
    public double bft;

    @ColumnInfo(name = "bultos")
    public double bultos;

    @ColumnInfo(name = "filas")
    public int filas;

    @ColumnInfo(name = "columnas")
    public int columnas;
}