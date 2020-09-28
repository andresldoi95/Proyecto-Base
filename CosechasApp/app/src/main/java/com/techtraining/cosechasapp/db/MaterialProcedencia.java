package com.techtraining.cosechasapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class MaterialProcedencia {
    @ColumnInfo(name = "material_id")
    public int materialId;

    @ColumnInfo(name = "procedencia_id")
    public int procedenciaId;
}
