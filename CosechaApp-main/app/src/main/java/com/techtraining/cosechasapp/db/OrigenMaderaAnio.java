package com.techtraining.cosechasapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class OrigenMaderaAnio {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "anio_cultivo")
    public String anio_cultivo;

    @ColumnInfo(name = "origen_madera_id")
    public int origen_madera_id;

    @ColumnInfo(name = "estado")
    public String estado;

    @Override
    public String toString() {
        return anio_cultivo;
    }
}
