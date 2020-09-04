package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EmpresaDao {
    @Query("SELECT * FROM empresa")
    List<Empresa> getAll();

    @Query("SELECT * FROM empresa WHERE id IN (:empresaIds)")
    List<Empresa> loadAllByIds(int[] empresaIds);

    @Query("SELECT * FROM empresa WHERE id = (:empresaId)")
    Empresa loadById(int empresaId);

    @Update
    void update(Empresa empresa);

    @Insert
    void insertOne(Empresa empresa);

    @Insert
    void insertAll(Empresa... empresas);

    @Delete
    void delete(Empresa empresa);
}
