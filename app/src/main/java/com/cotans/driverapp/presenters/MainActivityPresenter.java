package com.cotans.driverapp.presenters;


import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.db.Notification;
import com.cotans.driverapp.models.eventbus.MessageEvent;
import com.cotans.driverapp.views.Interfaces.MainActivityInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivityPresenter {

    MainActivityInterface view;

    DatabaseApi databaseApi;

    public MainActivityPresenter(DatabaseApi databaseApi) {
        this.databaseApi = databaseApi;
    }

    public void bindView(MainActivityInterface view) {
        this.view = view;
    }

    public void init() {
        beginNotificationReceiving();
    }

    public void beginNotificationReceiving() {
        EventBus.getDefault().register(this);
    }

    public void stopNotificationReciving() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        view.alertNewMessage(event.getMessageText());
    }
}
