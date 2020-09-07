package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface ParametroDao {
    @Query("SELECT * FROM parametro")
    List<Parametro> getAll();

    @Query("SELECT * FROM parametro")
    List<Parametro> getAllActive();

    @Query("SELECT * FROM parametro WHERE id IN (:parametrosIds)")
    List<Parametro> loadAllByIds(int[] parametrosIds);

    @Query("SELECT * FROM parametro WHERE id = (:parametroId)")
    Parametro loadById(int parametroId);

    @Update
    void update(Parametro parametro);

    @Insert
    void insertOne(Parametro parametro);

    @Insert
    void insertAll(Parametro... parametros);

    @Delete
    void delete(Parametro parametro);
}
