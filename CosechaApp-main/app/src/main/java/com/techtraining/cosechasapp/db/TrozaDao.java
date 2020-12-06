package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrozaDao {
    @Query("SELECT * FROM troza")
    List<Troza> getAll();

    @Query("SELECT * FROM troza WHERE id IN (:trozaIds)")
    List<Troza> loadAllByIds(int[] trozaIds);

    @Query("SELECT * FROM troza WHERE id = (:trozaId)")
    Troza loadById(String trozaId);

    @Query("SELECT * FROM troza WHERE cosecha_id = (:cosechaId)")
    Troza loadByCosecha(String cosechaId);

    @Update
    void update(Troza troza);

    @Insert
    void insertOne(Troza troza);

    @Insert
    void insertAll(Troza... trozas);

    @Delete
    void delete(Troza troza);
}
