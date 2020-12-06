package com.techtraining.cosechasapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Parametro {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "factor_hueco_sueltos")
    public double factorHuecoSueltos;

    @ColumnInfo(name = "factor_hueco_bultos")
    public double factorHuecoBultos;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;
}
