package com.spake.invent.ui;

import android.app.Application;

import com.spake.invent.database.AppDatabase;
import com.spake.invent.database.ItemDAO;
import com.spake.invent.database.entity.Item;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ItemsViewModel extends AndroidViewModel {
    private ItemDAO itemDao;
    private ExecutorService executorService;

    public ItemsViewModel(@NonNull Application application) {
        super(application);
        itemDao = AppDatabase.getInstance(application).itemDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Item>> getAll() {
        return itemDao.findAll();
    }

    public LiveData<Item> getSingle(int id) {
        return itemDao.find(id);
    }

    public LiveData<Item> getSingleByBarcode(String barcode) {
        return itemDao.findByBarcode(barcode);
    }

    public LiveData<List<Item>> getByStoragePlace(int storagePlaceId){ return itemDao.findByStoragePlace(storagePlaceId); }

    public LiveData<Integer> getCount(){ return itemDao.getCount(); };

    public void insert(Item item){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                itemDao.insertItem(item);
            }
        });
    }

    public void update(Item item){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                itemDao.updateItem(item);
            }
        });
    }

    public void remove(Item item) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                itemDao.deleteItem(item);
            }
        });
    }
}