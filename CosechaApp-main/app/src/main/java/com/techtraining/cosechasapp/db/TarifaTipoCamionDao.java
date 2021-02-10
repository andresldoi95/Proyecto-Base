package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TarifaDao {
    @Query("SELECT * FROM tarifa")
    List<Tarifa> getAll();

    @Query("SELECT * FROM tarifa WHERE estado = 'A'")
    List<Tarifa> getAllActive();

    @Query("SELECT * FROM tarifa WHERE id IN (:tarifaIds)")
    List<Tarifa> loadAllByIds(int[] tarifaIds);

    @Query("SELECT * FROM tarifa WHERE id = (:tarifaId)")
    Tarifa loadById(int tarifaId);

    /*@Query("SELECT * FROM tarifa WHERE origen_madera_id = (:origenMaderaId) AND destino_id = (:destinoId) AND tipo_camion LIKE :tipoCamion ")
    Tarifa loadByIdOrigenDestino(int origenMaderaId, int destinoId, String tipoCamion);*/

    @Update
    void update(Tarifa tarifa);

    @Insert
    void insertOne(Tarifa tarifa);

    @Insert
    void insertAll(Tarifa... tarifas);

    @Delete
    void delete(Tarifa tarifa);
}
