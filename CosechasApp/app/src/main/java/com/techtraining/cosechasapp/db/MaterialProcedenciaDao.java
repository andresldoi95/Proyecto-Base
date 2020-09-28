package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MaterialProcedenciaDao {
    @Query("SELECT * FROM materialprocedencia")
    List<MaterialProcedencia> getAll();


    @Query("SELECT * FROM materialprocedencia WHERE procedencia_id = (:procedenciaId)")
    List<MaterialProcedencia> loadByProcedencia(int procedenciaId);

    @Insert
    void insertOne(MaterialProcedencia materialProcedencia);

    @Insert
    void insertAll(MaterialProcedencia... materialesProcedencia);

    @Query("DELETE FROM materialprocedencia WHERE procedencia_id = :procedenciaId")
    void deleteByProcedencia(int procedenciaId);
}
