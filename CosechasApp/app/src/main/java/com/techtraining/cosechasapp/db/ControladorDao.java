package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface ControladorDao {
    @Query("SELECT * FROM controlador")
    List<Controlador> getAll();

    @Query("SELECT * FROM controlador WHERE estado = 'A' ORDER BY nombre")
    List<Controlador> getAllActive();

    @Query("SELECT * FROM controlador WHERE id IN (:controladoresIds)")
    List<Controlador> loadAllByIds(int[] controladoresIds);

    @Query("SELECT * FROM controlador WHERE id = (:controladorId)")
    Controlador loadById(int controladorId);

    @Update
    void update(Controlador controlador);

    @Insert
    void insertOne(Controlador controlador);

    @Insert
    void insertAll(Controlador... controladores);

    @Delete
    void delete(Controlador controlador);
}
