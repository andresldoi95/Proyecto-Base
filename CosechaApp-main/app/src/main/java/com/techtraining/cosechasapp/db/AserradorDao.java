package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface AserradorDao {
    @Query("SELECT * FROM aserrador")
    List<Aserrador> getAll();

    @Query("SELECT * FROM aserrador WHERE estado = 'A'")
    List<Aserrador> getAllActive();

    @Query("SELECT * FROM aserrador WHERE id IN (:aserradoresIds)")
    List<Aserrador> loadAllByIds(int[] aserradoresIds);

    @Query("SELECT * FROM aserrador WHERE id = (:aserradorId)")
    Aserrador loadById(int aserradorId);

    @Update
    void update(Aserrador aserrador);

    @Insert
    void insertOne(Aserrador aserrador);

    @Insert
    void insertAll(Aserrador... aserradores);

    @Delete
    void delete(Aserrador aserrador);
}
