package com.techtraining.cosechasapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(primaryKeys = {"origen_madera_id","hacienda_id"})
public class HaciendaMadera {
    @ColumnInfo(name = "origen_madera_id")
    public int origenMaderaId;

    @ColumnInfo(name = "hacienda_id")
    public int haciendaId;
}
