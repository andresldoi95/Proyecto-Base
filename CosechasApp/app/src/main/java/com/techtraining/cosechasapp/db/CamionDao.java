package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface CamionDao {
    @Query("SELECT * FROM camion")
    List<Camion> getAll();

    @Query("SELECT * FROM camion WHERE id IN (:camionesIds)")
    List<Camion> loadAllByIds(int[] camionesIds);

    @Query("SELECT * FROM camion WHERE id = (:camionId)")
    Camion loadById(int camionId);

    @Update
    void update(Camion camion);

    @Insert
    void insertOne(Camion camion);

    @Insert
    void insertAll(Camion... camiones);

    @Delete
    void delete(Camion camion);
}
