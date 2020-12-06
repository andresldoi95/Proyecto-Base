package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TipoBultoDao {
    @Query("SELECT * FROM tipobulto")
    List<TipoBulto> getAll();

    @Query("SELECT * FROM tipobulto WHERE estado = 'A'")
    List<TipoBulto> getAllActive();

    @Query("SELECT * FROM tipobulto WHERE id IN (:tipoBultoIds)")
    List<TipoBulto> loadAllByIds(int[] tipoBultoIds);

    @Query("SELECT * FROM tipobulto WHERE id = (:tipoBultoId)")
    TipoBulto loadById(int tipoBultoId);

    @Update
    void update(TipoBulto tipoBulto);

    @Insert
    void insertOne(TipoBulto tipoBulto);

    @Insert
    void insertAll(TipoBulto... tiposBulto);

    @Delete
    void delete(TipoBulto tipoBulto);
}
