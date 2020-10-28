package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HaciendaMaderaDao {
    @Query("SELECT * FROM haciendamadera")
    List<HaciendaMadera> getAll();

    @Query("SELECT * FROM haciendamadera WHERE origen_madera_id IN (:origenMaderaIds)")
    List<HaciendaMadera> loadByOrigenMadera(int[] origenMaderaIds);

    @Insert
    void insertOne(HaciendaMadera haciendaMadera);
}
