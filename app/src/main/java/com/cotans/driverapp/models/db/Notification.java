package com.cotans.driverapp.models.db;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "notifications")
public class Notification {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "timestamp")
    private long timestamp;
    @ColumnInfo(name = "text")
    private String text;

    public Notification(long timestamp, String text) {
        this.timestamp = timestamp;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
