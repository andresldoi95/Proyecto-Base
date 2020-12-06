package com.techtraining.cosechasapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TipoBulto {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "codigo")
    public String codigo;

    @ColumnInfo(name = "largo_id")
    public int largoId;

    @ColumnInfo(name = "espesor_id")
    public int espesorId;

    @ColumnInfo(name = "ancho")
    public double ancho;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "estado")
    public String estado;

    @Override
    public String toString() {
        return codigo + " (" + this.ancho + ")";
    }
}
