package com.spake.invent.ui;

import android.app.Application;
import android.util.Log;

import com.spake.invent.database.AppDatabase;
import com.spake.invent.database.ItemDAO;
import com.spake.invent.database.StoragePlaceDAO;
import com.spake.invent.database.entity.Item;
import com.spake.invent.database.entity.StoragePlace;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class StoragePlaceViewModel extends AndroidViewModel {
    private StoragePlaceDAO storagePlaceDAO;
    private ExecutorService executorService;

    public StoragePlaceViewModel(@NonNull Application application) {
        super(application);
        storagePlaceDAO = AppDatabase.getInstance(application).storagePlaceDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Gets all StoragePlace objects from database.
     * @return LiveData StoragePlace list.
     */
    public LiveData<List<StoragePlace>> getAll(StoragePlace.Type type) {
        return storagePlaceDAO.findStoragePlaceByType(type.getCode());
    }

    /**
     * Get single StoragePlace object from database.
     * @param id id of StoragePlace in database.
     * @return LiveData StoragePlace object.
     */
    public LiveData<StoragePlace> getSingle(int id) {
        return storagePlaceDAO.find(id);
    }

    /**
     * Inserts new StoragePlace object into database
     * @param storagePlace
     */
    public void insert(StoragePlace storagePlace){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                storagePlaceDAO.insertStoragePlace(storagePlace);
            }
        });
    }

    /**
     * Updates one StoragePlace object in database
     * @param storagePlace object
     */
    public void update(StoragePlace storagePlace){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                storagePlaceDAO.updateStoragePlace(storagePlace);
            }
        });
    }

    /**
     * Removes one StoragePlace from database
     * @param storagePlace entity object
     */
    public void remove(StoragePlace storagePlace) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                storagePlaceDAO.deleteStoragePlace(storagePlace);
            }
        });
    }
}