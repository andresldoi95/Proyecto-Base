package com.techtraining.cosechasapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface LargoDao {
    @Query("SELECT * FROM largo")
    List<Largo> getAll();

    @Query("SELECT * FROM largo WHERE id IN (:largosIds)")
    List<Largo> loadAllByIds(int[] largosIds);

    @Query("SELECT * FROM largo WHERE id = (:largoId)")
    Largo loadById(int largoId);

    @Update
    void update(Largo largo);

    @Insert
    void insertOne(Largo largo);

    @Insert
    void insertAll(Largo... largos);

    @Delete
    void delete(Largo largo);
}
