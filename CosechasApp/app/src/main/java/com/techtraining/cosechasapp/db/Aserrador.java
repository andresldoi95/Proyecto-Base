package com.techtraining.cosechasapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Aserrador {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "identificacion")
    public String identificacion;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "estado")
    public String estado;

    @Override
    public String toString() {
        return nombre + " (" + identificacion + ")";
    }
}