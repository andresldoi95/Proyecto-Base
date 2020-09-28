package com.techtraining.cosechasapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class OrigenMadera {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "descripcion")
    public String descripcion;

    @ColumnInfo(name = "anio_cultivo")
    public int anioCultivo;

    @ColumnInfo(name = "volumen_inventario")
    public double volumenInventario;

    @ColumnInfo(name = "hectareas")
    public double hectareas;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "estado")
    public String estado;

    @Override
    public String toString() {
        return descripcion + " (" + this.anioCultivo + ")";
    }
}
