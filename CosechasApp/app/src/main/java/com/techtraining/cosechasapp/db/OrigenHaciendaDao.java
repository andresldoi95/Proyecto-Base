package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrigenHaciendaDao {
    @Query("SELECT * FROM origenhacienda")
    List<OrigenHacienda> getAll();

    @Query("SELECT * FROM origenhacienda WHERE estado = 'A'")
    List<OrigenHacienda> getAllActive();

    @Query("SELECT * FROM origenhacienda WHERE id IN (:origenHaciendaIds)")
    List<OrigenHacienda> loadAllByIds(int[] origenHaciendaIds);

    @Query("SELECT * FROM origenhacienda WHERE id = (:origenHaciendaId)")
    OrigenHacienda loadById(int origenHaciendaId);

    @Update
    void update(OrigenHacienda origenHacienda);

    @Insert
    void insertOne(OrigenHacienda origenHacienda);

    @Insert
    void insertAll(OrigenHacienda... origenesHacienda);

    @Delete
    void delete(OrigenHacienda origenHacienda);
}
