package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface ProcedenciaDao {
    @Query("SELECT * FROM procedencia")
    List<Procedencia> getAll();

    @Query("SELECT * FROM procedencia WHERE id IN (:procedenciasIds)")
    List<Procedencia> loadAllByIds(int[] procedenciasIds);

    @Query("SELECT * FROM procedencia WHERE id = (:procedenciaId)")
    Procedencia loadById(int procedenciaId);

    @Update
    void update(Procedencia procedencia);

    @Insert
    void insertOne(Procedencia procedencia);

    @Insert
    void insertAll(Procedencia... procedencias);

    @Delete
    void delete(Procedencia procedencia);
}
