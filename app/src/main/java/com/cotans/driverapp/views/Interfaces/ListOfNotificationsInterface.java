package com.cotans.driverapp.views.Interfaces;


import com.cotans.driverapp.models.db.Notification;

import java.util.List;

public interface ListOfNotificationsInterface {

    public void fill(List<Notification> notifications);

    void stopRefreshDelayed(int i);
}
