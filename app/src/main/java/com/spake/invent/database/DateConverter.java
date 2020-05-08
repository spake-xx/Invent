package com.spake.invent.database;

import android.util.Log;

import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp){
        Log.i("CONVERT", "Przekonwertowano timestamp "+timestamp);
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
