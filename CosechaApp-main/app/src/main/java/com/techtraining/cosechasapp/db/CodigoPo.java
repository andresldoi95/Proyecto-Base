package com.techtraining.cosechasapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Material {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "descripcion")
    public String descripcion;

    @ColumnInfo(name = "codigo")
    public String codigo;

    @ColumnInfo(name = "empresa_id")
    public int empresaId;

    @ColumnInfo(name = "estado")
    public String estado;

    @ColumnInfo(name = "tipo_madera_id")
    public int tipoMaderaId;

    @ColumnInfo(name = "origen_madera_id")
    public int origenMaderaId;

    public String toString() {
        return this.descripcion + " (" + this.codigo + ")";
    }
}
