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
public interface OrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrders(Order... orders);

    @Delete
    void deleteOrders(Order... orders);

    @Update
    void updateOrders(Order... orders);

    @Query("SELECT * FROM orders WHERE id = :id")
    Maybe<Order> getOrderById(int id);

    @Query("SELECT * from orders")
    Flowable<List<Order>> getAllOrders();

    //TODO refactor this, kind of duplicate method
    @Query("SELECT * FROM orders WHERE id = :id")
    Order getOneOrder(int id);

    @Query("SELECT * from orders WHERE isDelivered = 0")
    Flowable<List<Order>> getAllUndeliveredOrders();

    @Query("SELECT * from orders WHERE isNew = 1 AND isDelivered = 0")
    Flowable<List<Order>> getAllNewOrders();

    @Query("SELECT * from orders WHERE isNew = 0 AND isDelivered = 0")
    Flowable<List<Order>> getAllCurrentOrders();

    @Query("SELECT COUNT(*) from orders")
    Single<Integer> getNumberOfAllOrders();

    @Query("DELETE FROM orders")
    void clearTable();
}
