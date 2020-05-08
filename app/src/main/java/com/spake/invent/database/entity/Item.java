package com.spake.invent.database.entity;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "item", foreignKeys = @ForeignKey(entity = StoragePlace.class,
        onDelete = CASCADE,
        parentColumns = "id",
        childColumns = "storagePlaceId" ))
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String barcode;
    private String name;
    private String description;
    private Date createdAt;
    private Date expireAt;
    public int storagePlaceId;


    public Item(String barcode, String name, String description, int storagePlaceId, Date expireAt) {
        this.createdAt = new Date();;
        this.barcode = barcode;
        this.name = name;
        this.description = description;
        this.storagePlaceId = storagePlaceId;
        this.expireAt = expireAt;
    }

    @Ignore
    public Item(int id, String barcode, String name, String description, int storagePlaceId, Date expireAt) {
        this.createdAt = new Date();;
        this.barcode = barcode;
        this.name = name;
        this.description = description;
        this.storagePlaceId = storagePlaceId;
        this.id = id;
        this.expireAt = expireAt;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getStoragePlaceId() {
        return storagePlaceId;
    }

    public void setStoragePlaceId(int storagePlaceId) {
        this.storagePlaceId = storagePlaceId;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }
}
