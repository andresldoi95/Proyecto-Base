package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface CodigoAserradorDao {
    @Query("SELECT * FROM codigoAserrador")
    List<CodigoAserrador> getAll();

    @Query("SELECT * FROM codigoAserrador WHERE id IN (:codigosAserradoresId)")
    List<CodigoAserrador> loadAllByIds(int[] codigosAserradoresId);

    @Query("SELECT * FROM codigoAserrador WHERE id = (:codigoAserradorId)")
    CodigoAserrador loadById(int codigoAserradorId);

    @Update
    void update(CodigoAserrador codigoAserrador);

    @Insert
    void insertOne(CodigoAserrador codigoAserrador);

    @Insert
    void insertAll(CodigoAserrador... codigosAserradores);

    @Delete
    void delete(CodigoAserrador codigoAserrador);
}
