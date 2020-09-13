package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FilaCosechaDao {

    @Query("SELECT * FROM filacosecha WHERE id = (:id)")
    FilaCosecha loadById(String id);

    @Query("SELECT * FROM filacosecha WHERE cosecha_id = (:cosechaId) ORDER BY indice")
    List<FilaCosecha> loadByCosecha(String cosechaId);

    @Update
    void update(FilaCosecha filaCosecha);

    @Insert
    void insertOne(FilaCosecha filaCosecha);

    @Insert
    void insertAll(FilaCosecha... filasCosecha);

    @Delete
    void delete(FilaCosecha filaCosecha);

    @Query("DELETE FROM filacosecha WHERE cosecha_id = :cosechaId")
    void deleteByCosecha(String cosechaId);
}
