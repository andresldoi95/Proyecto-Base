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

    @ColumnInfo(name = "tipo_bulto_id")
    public int tipoBultoId;

    @ColumnInfo(name = "indice")
    public int indice;

    @ColumnInfo(name = "bft")
    public double bft;

    @ColumnInfo(name = "bultos")
    public int bultos;

    @ColumnInfo(name = "estado")
    public String estado;

    @ColumnInfo(name = "tipo")
    public String tipo;
}
