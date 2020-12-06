package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TrozaFotosDao {
    @Query("SELECT * FROM trozafotos")
    List<TrozaFotos> getAll();

    @Query("SELECT * FROM trozafotos WHERE troza_id = (:trozaId)")
    List<TrozaFotos> loadAllByTrozaId(String trozaId);

    @Update
    void update(TrozaFotos trozaFotos);

    @Insert
    void insertOne(TrozaFotos trozaFotos);

    @Insert
    void insertAll(TrozaFotos... trozaFotos);

    @Delete
    void delete(TrozaFotos trozaFotos);
}
