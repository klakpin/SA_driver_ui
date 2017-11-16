package com.cotans.driverapp.models.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Order.class, Notification.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract OrderDao OrderDao();
    public abstract NotificationDao notificationDao();

}
