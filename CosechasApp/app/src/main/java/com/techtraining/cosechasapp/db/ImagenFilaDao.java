package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ImagenFilaDao {
    @Query("SELECT * FROM imagenfila WHERE id = (:id)")
    ImagenFila loadById(String id);

    @Query("SELECT * FROM imagenfila WHERE fila_id = (:filaId)")
    List<ImagenFila> loadByFila(String filaId);

    @Update
    void update(ImagenFila imagenFila);

    @Insert
    void insertOne(ImagenFila imagenFila);

    @Insert
    void insertAll(ImagenFila... imagenesFila);

    @Delete
    void delete(ImagenFila imagenFila);

    @Query("DELETE FROM imagenfila WHERE fila_id = :filaId")
    void deleteByFila(String filaId);
}
