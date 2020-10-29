package com.techtraining.cosechasapp.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ItemFilaCosecha {
    @NonNull
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "fila_id")
    public String filaId;

    @ColumnInfo(name = "bft")
    public double bft;

    @ColumnInfo(name = "bultos")
    public int bultos;

    @ColumnInfo(name = "fila")
    public int fila;

    @ColumnInfo(name = "columna")
    public int columna;

    @ColumnInfo(name = "plantilla")
    public double plantilla;

    @ColumnInfo(name = "espesor_id")
    public int espesorId;

    @ColumnInfo(name = "largo_id")
    public int largoId;
}
