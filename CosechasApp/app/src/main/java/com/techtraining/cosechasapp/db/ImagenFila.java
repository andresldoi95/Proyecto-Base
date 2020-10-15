package com.techtraining.cosechasapp.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ImagenFila {
    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "fila_id")
    public int filaId;

    @ColumnInfo(name = "path")
    public String path;
}
