package com.cotans.driverapp.models.db;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNotifications(Notification... notifications);

    @Delete
    int deleteNotifications(Notification... notifications);

    @Update
    void updateNotifications(Notification... notifications);

    @Query("SELECT * FROM notifications")
    Flowable<List<Notification>> getAllNotifications();

    @Query("SELECT * FROM notifications WHERE id = :id")
    Maybe<Notification> getNotificationById(int id);

    @Query("SELECT COUNT(*) FROM notifications")
    Single<Integer> getNotificationsNumber();

    @Query("DELETE FROM notifications")
    void clearTable();
}
