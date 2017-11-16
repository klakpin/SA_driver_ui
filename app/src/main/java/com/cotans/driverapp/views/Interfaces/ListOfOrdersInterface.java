package com.cotans.driverapp.views.Interfaces;

import android.content.Context;

import com.cotans.driverapp.models.db.Order;


import java.util.List;

public interface ListOfOrdersInterface {

    void fill(List<Order> orders);

    void setNewOrders(List<Order> orders);

    void setCurrentOrders(List<Order> orders);

    void onErrorWhileDownloadingOrders(String s);

    void clearList();

    void notifyDataChanged();
}
