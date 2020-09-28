package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FormatoEntregaDao {
    @Query("SELECT * FROM formatoentrega")
    List<FormatoEntrega> getAll();

    @Query("SELECT * FROM formatoentrega WHERE estado = 'A'")
    List<FormatoEntrega> getAllActive();

    @Query("SELECT * FROM formatoentrega WHERE id IN (:formatoEntregaIds)")
    List<FormatoEntrega> loadAllByIds(int[] formatoEntregaIds);

    @Query("SELECT * FROM formatoentrega WHERE id = (:formatoEntregaId)")
    FormatoEntrega loadById(int formatoEntregaId);

    @Update
    void update(FormatoEntrega formatoEntrega);

    @Insert
    void insertOne(FormatoEntrega formatoEntrega);

    @Insert
    void insertAll(FormatoEntrega... formatosEntrega);

    @Delete
    void delete(FormatoEntrega formatoEntrega);
}
