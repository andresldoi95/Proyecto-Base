package com.techtraining.cosechasapp.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TrozaFotos {
    @PrimaryKey
    @NonNull
    public String id;
    @ColumnInfo(name = "troza_id")
    public String trozaId;
    @ColumnInfo(name = "foto")
    public String foto;
}
