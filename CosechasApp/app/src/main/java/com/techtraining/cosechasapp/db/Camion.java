package com.techtraining.cosechasapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Camion {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "tipo_camion")
    public String tipoCamion;

    @ColumnInfo(name = "ancho")
    public double ancho;

    @ColumnInfo(name = "placa")
    public String placa;

    @ColumnInfo(name = "alto")
    public double alto;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "camionero")
    public String camionero;

    @ColumnInfo(name = "identificacion_camionero")
    public String identificacionCamionero;

    @ColumnInfo(name = "estado")
    public String estado;
    @Override
    public String toString() {
        return this.placa;
    }
}
