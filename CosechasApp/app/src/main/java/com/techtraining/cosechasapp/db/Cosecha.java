package com.techtraining.cosechasapp.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cosecha {
    @PrimaryKey @NonNull
    public String id;

    @ColumnInfo(name = "camion_id")
    public int camionId;

    @ColumnInfo(name = "controlador_id")
    public int controladorId;

    @ColumnInfo(name = "destino_id")
    public int destinoId;

    @ColumnInfo(name = "origen_id")
    public int origenId;

    @ColumnInfo(name = "material_id")
    public int materialId;

    @ColumnInfo(name = "codigo_po")
    public String codigoPo;

    @ColumnInfo(name = "estado")
    public String estado;
}
