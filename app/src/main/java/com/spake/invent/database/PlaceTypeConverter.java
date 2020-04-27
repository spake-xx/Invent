package com.spake.invent.database;

import com.spake.invent.database.entity.StoragePlace;

import androidx.room.TypeConverter;

public class PlaceTypeConverter {
    @TypeConverter
    public static StoragePlace.Type toType(int type) {
        if (type == StoragePlace.Type.BAG.getCode()) {
            return StoragePlace.Type.BAG;
        } else if (type == StoragePlace.Type.LOCKER.getCode()) {
            return StoragePlace.Type.LOCKER;
        } else if (type == StoragePlace.Type.VEHICLE.getCode()) {
            return StoragePlace.Type.VEHICLE;
        } else {
            throw new IllegalArgumentException("Could not recognize type");
        }
    }

    @TypeConverter
    public static int toInteger(StoragePlace.Type type) {
        return type.getCode();
    }
}