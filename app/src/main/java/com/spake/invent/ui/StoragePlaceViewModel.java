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

    public LiveData<List<StoragePlace>> getAll(StoragePlace.Type type) {
        return storagePlaceDAO.findStoragePlaceByType(type.getCode());
    }

    public LiveData<StoragePlace> getSingle(int id) {
        return storagePlaceDAO.find(id);
    }

    public void insert(StoragePlace sp){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                storagePlaceDAO.insertStoragePlace(sp);
            }
        });
    }

    public void remove(StoragePlace storagePlace) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                storagePlaceDAO.deleteStoragePlace(storagePlace);
            }
        });
    }
}