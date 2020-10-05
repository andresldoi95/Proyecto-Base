package com.techtraining.cosechasapp.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FilaSuelto {
    @NonNull
    @PrimaryKey
    public String id;
    @ColumnInfo(name = "fila_id")
    public String filaId;

    @ColumnInfo(name = "largo_id")
    public int largoId;

    @ColumnInfo(name = "espesor_id")
    public int espesorId;

    @ColumnInfo(name = "indice")
    public int indice;

    @ColumnInfo(name = "bft")
    public double bft;

    @ColumnInfo(name = "bultos")
    public double bultos;
}
