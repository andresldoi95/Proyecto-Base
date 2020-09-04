package com.techtraining.cosechasapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Largo {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "descripcion")
    public String descripcion;

    @ColumnInfo(name = "valor")
    public double valor;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "estado")
    public String estado;
}
