package com.spake.invent.database;

import com.spake.invent.database.entity.Item;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ItemDAO {
    @Query("SELECT * FROM item ORDER BY createdAt")
    List<Item> loadAllItems();

    @Insert
    void insertItem(Item item);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateItem(Item item);

    @Delete
    void deleteItem(Item item);
}
