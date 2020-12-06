package com.techtraining.cosechasapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tarifa {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "destino_id")
    public int destinoId;

    @ColumnInfo(name = "origen_madera_id")
    public int origenMaderaId;

    @ColumnInfo(name = "valor")
    public double valor;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "estado")
    public String estado;
}
