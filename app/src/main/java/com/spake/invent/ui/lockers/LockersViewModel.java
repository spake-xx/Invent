package com.spake.invent.ui.lockers;

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
import androidx.lifecycle.ViewModel;

public class LockersViewModel extends AndroidViewModel {
    private ItemDAO itemDao;
    private ExecutorService executorService;

    public LockersViewModel(@NonNull Application application) {
        super(application);
        itemDao = AppDatabase.getInstance(application).itemDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Item>> getAll() {
        return itemDao.findAll();
    }

    public void remove(Item item) {
        itemDao.deleteItem(item);
    }
}