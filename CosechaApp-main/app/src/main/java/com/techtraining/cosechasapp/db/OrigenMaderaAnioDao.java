package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrigenMaderaDao {
    @Query("SELECT * FROM origenmadera")
    List<OrigenMadera> getAll();

    @Query("SELECT * FROM origenmadera WHERE estado = 'A'")
    List<OrigenMadera> getAllActive();

    @Query("SELECT * FROM origenmadera WHERE id IN (:origenMaderaIds)")
    List<OrigenMadera> loadAllByIds(int[] origenMaderaIds);

    @Query("SELECT * FROM origenmadera WHERE id = (:origenMaderaId)")
    OrigenMadera loadById(int origenMaderaId);

    @Update
    void update(OrigenMadera origenMadera);

    @Insert
    void insertOne(OrigenMadera origenMadera);

    @Insert
    void insertAll(OrigenMadera... origenesMadera);

    @Delete
    void delete(OrigenMadera origenMadera);
}
