package com.techtraining.cosechasapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Parametro {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "factor_hueco")
    public double factorHueco;

    @ColumnInfo(name = "constante")
    public double constante;

    @ColumnInfo(name = "ancho_bulto")
    public double anchoBulto;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;
}
