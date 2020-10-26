package com.techtraining.cosechasapp.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
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

    @ColumnInfo(name = "aserrador_id")
    public int aserradorId;

    @ColumnInfo(name = "material_id")
    public int materialId;

    @ColumnInfo(name = "tipo_madera_id")
    public int tipoMaderaId;

    @ColumnInfo(name = "origen_madera_id")
    public int origenMaderaId;

    @ColumnInfo(name = "formato_entrega_id")
    public int formatoEntregaId;

    @ColumnInfo(name = "codigo_po")
    public String codigoPo;

    @ColumnInfo(name = "fecha_tumba")
    public String fechaTumba;

    @ColumnInfo(name = "fecha_despacho")
    public String fechaDespacho;

    @ColumnInfo(name = "dias_t2k")
    public String diasT2k;

    @ColumnInfo(name = "guia_remision")
    public String guiaRemision;

    @ColumnInfo(name = "guia_forestal")
    public String guiaForestal;

    @ColumnInfo(name = "tipo_llenado")
    public String tipoLlenado;

    @ColumnInfo(name = "valor_flete")
    public double valorFlete;

    @ColumnInfo(name = "estado")
    public String estado;

    @Ignore
    public boolean seleccionado;
}
