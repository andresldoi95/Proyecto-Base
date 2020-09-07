package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CosechaDao {
    @Query("SELECT * FROM cosecha")
    List<Cosecha> getAll();

    @Query("SELECT * FROM cosecha WHERE estado = 'P' ORDER BY codigo_po")
    List<Cosecha> getPendientes();

    @Query("SELECT * FROM cosecha WHERE estado = 'F' ORDER BY codigo_po")
    List<Cosecha> getFinalizadas();

    @Query("SELECT * FROM cosecha WHERE id IN (:cosechasIds)")
    List<Cosecha> loadAllByIds(String[] cosechasIds);

    @Query("SELECT * FROM cosecha WHERE id = (:cosechaId)")
    Cosecha loadById(String cosechaId);

    @Update
    void update(Cosecha cosecha);

    @Insert
    void insertOne(Cosecha cosecha);

    @Insert
    void insertAll(Cosecha... cosechas);

    @Delete
    void delete(Cosecha cosecha);
}
