package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrigenMaderaAnioDao {
    @Query("SELECT * FROM origenmaderaanio")
    List<OrigenMaderaAnio> getAll();

    @Query("SELECT * FROM origenmaderaanio WHERE estado = 'A'")
    List<OrigenMaderaAnio> getAllActive();

    @Query("SELECT * FROM origenmaderaanio WHERE estado = 'A' AND origen_madera_id = :select_origen_madera_id ")
    List<OrigenMaderaAnio> getAllActiveOrigenMadera(int select_origen_madera_id);

    @Query("SELECT * FROM origenmaderaanio WHERE id IN (:origenMaderaAnioIds)")
    List<OrigenMaderaAnio> loadAllByIds(int[] origenMaderaAnioIds);

    @Query("SELECT * FROM origenmaderaanio WHERE id = (:origenMaderaAnioId)")
    OrigenMaderaAnio loadById(int origenMaderaAnioId);

    @Update
    void update(OrigenMaderaAnio origenMaderaAnio);

    @Insert
    void insertOne(OrigenMaderaAnio origenMaderaAnio);

    @Insert
    void insertAll(OrigenMaderaAnio... origenMaderaAnios);

    @Delete
    void delete(OrigenMaderaAnio origenMaderaAnio);
}
