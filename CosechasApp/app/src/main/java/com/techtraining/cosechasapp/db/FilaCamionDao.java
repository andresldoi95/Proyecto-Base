package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface FilaCamionDao {
    @Query("SELECT * FROM filacamion")
    List<FilaCamion> getAll();

    @Query("SELECT * FROM filacamion WHERE id IN (:filasCamionId)")
    List<FilaCamion> loadAllByIds(int[] filasCamionId);

    @Query("SELECT * FROM filacamion WHERE id = (:filaCamionId)")
    FilaCamion loadById(String filaCamionId);

    @Query("SELECT * FROM filacamion WHERE camion_id = (:camionId)")
    List<FilaCamion> loadByCamion(int camionId);

    @Update
    void update(FilaCamion filaCamion);

    @Insert
    void insertOne(FilaCamion filaCamion);

    @Insert
    void insertAll(FilaCamion... filasCamion);

    @Delete
    void delete(FilaCamion filaCamion);

    @Query("DELETE FROM filacamion WHERE camion_id = :camionId")
    void deleteByCamion(int camionId);

}
