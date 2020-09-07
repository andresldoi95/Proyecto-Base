package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface DestinoDao {
    @Query("SELECT * FROM destino")
    List<Destino> getAll();

    @Query("SELECT * FROM destino WHERE estado = 'A' ORDER BY descripcion")
    List<Destino> getAllActive();

    @Query("SELECT * FROM destino WHERE id IN (:destinosIds)")
    List<Destino> loadAllByIds(int[] destinosIds);

    @Query("SELECT * FROM destino WHERE id = (:destinoId)")
    Destino loadById(int destinoId);

    @Update
    void update(Destino destino);

    @Insert
    void insertOne(Destino destino);

    @Insert
    void insertAll(Destino... destinos);

    @Delete
    void delete(Destino destino);
}
