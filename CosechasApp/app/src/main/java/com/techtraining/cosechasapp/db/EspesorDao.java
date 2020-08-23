package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface EspesorDao {
    @Query("SELECT * FROM espesor")
    List<Espesor> getAll();

    @Query("SELECT * FROM espesor WHERE id IN (:espesoresIds)")
    List<Espesor> loadAllByIds(int[] espesoresIds);

    @Query("SELECT * FROM espesor WHERE id = (:espesorId)")
    Espesor loadById(int espesorId);

    @Update
    void update(Espesor espesor);

    @Insert
    void insertOne(Espesor espesor);

    @Insert
    void insertAll(Espesor... espesores);

    @Delete
    void delete(Espesor espesor);
}
