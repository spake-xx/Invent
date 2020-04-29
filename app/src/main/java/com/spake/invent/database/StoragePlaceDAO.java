package com.spake.invent.database;

import com.spake.invent.database.entity.Item;
import com.spake.invent.database.entity.StoragePlace;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface StoragePlaceDAO {
    @Query("SELECT * FROM storage_place ORDER BY createdAt")
    LiveData<List<StoragePlace>> findAll();

    @Query("SELECT * FROM storage_place WHERE id=:id")
    LiveData<StoragePlace> find(int id);

    @Query("SELECT * FROM storage_place WHERE type=:type")
    LiveData<List<StoragePlace>> findStoragePlaceByType(final int type);

    @Insert
    void insertStoragePlace(StoragePlace storagePlace);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStoragePlace(StoragePlace storagePlace);

    @Delete
    void deleteStoragePlace(StoragePlace storagePlace);
}
