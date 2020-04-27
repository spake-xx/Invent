package com.spake.invent.database;

import android.content.Context;
import android.util.Log;

import com.spake.invent.database.entity.Item;
import com.spake.invent.database.entity.StoragePlace;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Item.class, StoragePlace.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class, PlaceTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "barcodes";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context){
        if (sInstance == null){
            synchronized(LOCK){
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract ItemDAO itemDao();
    public abstract StoragePlaceDAO storagePlaceDao();
}
