package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TipoMaderaDao {
    @Query("SELECT * FROM tipomadera")
    List<TipoMadera> getAll();

    @Query("SELECT * FROM tipomadera WHERE estado = 'A'")
    List<TipoMadera> getAllActive();

    @Query("SELECT * FROM tipomadera WHERE id IN (:tipoMaderaIds)")
    List<TipoMadera> loadAllByIds(int[] tipoMaderaIds);

    @Query("SELECT * FROM tipomadera WHERE id = (:tipoMaderaId)")
    TipoMadera loadById(int tipoMaderaId);

    @Update
    void update(TipoMadera tipoMadera);

    @Insert
    void insertOne(TipoMadera tipoMadera);

    @Insert
    void insertAll(TipoMadera... tiposMadera);

    @Delete
    void delete(TipoMadera tipoMadera);
}
