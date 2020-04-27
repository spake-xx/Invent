package com.spake.invent.ui.bags;

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

public class BagsViewModel extends AndroidViewModel {
    private StoragePlaceDAO storagePlaceDAO;
    private ExecutorService executorService;

    public BagsViewModel(@NonNull Application application) {
        super(application);
        storagePlaceDAO = AppDatabase.getInstance(application).storagePlaceDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<StoragePlace>> getAll() {
        return storagePlaceDAO.findStoragePlaceByType(StoragePlace.Type.BAG.getCode());
    }

    public void remove(StoragePlace storagePlace) {
        storagePlaceDAO.deleteStoragePlace(storagePlace);
    }
}