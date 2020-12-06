package com.techtraining.cosechasapp.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Troza {
    @PrimaryKey
    @NonNull
    public String id;
    @ColumnInfo(name = "cosecha_id")
    public String cosechaId;
    @ColumnInfo(name = "numero_trozas")
    public double numeroTrozas;
    @ColumnInfo(name = "volumen_estimado")
    public double volumenEstimado;
    @ColumnInfo(name = "observaciones")
    public String observaciones;
}
