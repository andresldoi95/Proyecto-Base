package com.techtraining.cosechasapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FormatoEntrega {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "descripcion")
    public String descripcion;


    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "estado")
    public String estado;

    @ColumnInfo(name = "factor_hueco")
    public double factorHueco;

    @Override
    public String toString() {
        return descripcion;
    }
}
