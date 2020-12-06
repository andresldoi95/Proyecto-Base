package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FilaSueltoDao {
    @Query("SELECT * FROM filasuelto WHERE id = (:id)")
    FilaSuelto loadById(String id);

    @Query("SELECT * FROM filasuelto WHERE fila_id = (:filaId) ORDER BY indice")
    List<FilaSuelto> loadByFila(String filaId);

    @Update
    void update(FilaSuelto filaSuelto);

    @Insert
    void insertOne(FilaSuelto filaSuelto);

    @Insert
    void insertAll(FilaSuelto... filasSuelto);

    @Delete
    void delete(FilaSuelto filaSuelto);

    @Query("DELETE FROM filasuelto WHERE fila_id = :filaId")
    void deleteByFila(String filaId);
}

