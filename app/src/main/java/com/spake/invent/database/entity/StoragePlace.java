package com.spake.invent.database.entity;

import com.spake.invent.database.PlaceTypeConverter;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "storage_place")
public class StoragePlace {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String description;
    private Date createdAt;
    private Type type;


    public StoragePlace(String name, Type type, String description, Date createdAt) {
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.type = type;
    }

    public enum Type {
        BAG(0),
        LOCKER(1),
        VEHICLE(2);

        private int code;

        Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
