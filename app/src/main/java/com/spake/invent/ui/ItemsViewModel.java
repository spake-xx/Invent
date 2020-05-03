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

public class ItemsViewModel extends AndroidViewModel {
    private ItemDAO itemDao;
    private ExecutorService executorService;

    public ItemsViewModel(@NonNull Application application) {
        super(application);
        itemDao = AppDatabase.getInstance(application).itemDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Gets all Item objects from database.
     * @return LiveData Item list.
     */
    public LiveData<List<Item>> getAll() {
        return itemDao.findAll();
    }

    /**
     * Get single Item object from database.
     * @param id id of Item in database.
     * @return LiveData Item object.
     */
    public LiveData<Item> getSingle(int id) {
        return itemDao.find(id);
    }

    /**
     * Get single Item object from database.
     * @param barcode of Item object in database.
     * @return LiveData Item object.
     */
    public LiveData<Item> getSingle(String barcode) {
        return itemDao.findByBarcode(barcode);
    }

    /**
     * Get LiveData Item List from database.
     * @param storagePlaceId Item value to retrieve from database.
     * @return LiveData Item List.
     */
    public LiveData<List<Item>> getByStoragePlace(int storagePlaceId){ return itemDao.findByStoragePlace(storagePlaceId); }

    /**
     * Gets total Item count in database.
     * @return LiveData Integer containing count of items
     */
    public LiveData<Integer> getCount(){ return itemDao.getCount(); };

    /**
     * Inserts new Item object into database
     * @param item entity object
     */
    public void insert(Item item){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                itemDao.insertItem(item);
            }
        });
    }

    /**
     * Updates one Item object in database
     * @param item entity object
     */
    public void update(Item item){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                itemDao.updateItem(item);
            }
        });
    }

    /**
     * Removes one Item from database
     * @param item entity object
     */
    public void remove(Item item) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                itemDao.deleteItem(item);
            }
        });
    }
}