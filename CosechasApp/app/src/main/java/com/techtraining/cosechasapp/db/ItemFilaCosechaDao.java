package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemFilaCosechaDao {
    @Query("SELECT * FROM itemfilacosecha WHERE id = (:id)")
    ItemFilaCosecha loadById(String id);

    @Query("SELECT * FROM itemfilacosecha WHERE fila_id = (:filaId) ORDER BY fila, columna")
    List<ItemFilaCosecha> loadByFila(String filaId);

    @Update
    void update(ItemFilaCosecha itemFilaCosecha);

    @Insert
    void insertOne(ItemFilaCosecha itemFilaCosecha);

    @Insert
    void insertAll(ItemFilaCosecha... itemsFilaCosecha);

    @Delete
    void delete(ItemFilaCosecha itemFilaCosecha);

    @Query("DELETE FROM itemfilacosecha WHERE fila_id = :filaId")
    void deleteByFila(String filaId);
}
