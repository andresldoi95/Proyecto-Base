package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface MaterialDao {
    @Query("SELECT * FROM material")
    List<Material> getAll();

    @Query("SELECT * FROM material WHERE estado = 'A' ORDER BY descripcion")
    List<Material> getAllActive();

    @Query("SELECT * FROM material WHERE id IN (:materialsIds)")
    List<Material> loadAllByIds(int[] materialsIds);

    @Query("SELECT * FROM material WHERE id = (:materialId)")
    Material loadById(int materialId);

    @Query("SELECT * FROM material WHERE tipo_madera_id = (:tipoMaderaId) AND origen_madera_id = (:origenMaderaId)")
    Material loadByIdTipos(int tipoMaderaId, int origenMaderaId);

    @Update
    void update(Material material);

    @Insert
    void insertOne(Material material);

    @Insert
    void insertAll(Material... materiales);

    @Delete
    void delete(Material material);
}
