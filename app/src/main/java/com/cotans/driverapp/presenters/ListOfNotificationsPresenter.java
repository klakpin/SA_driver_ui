package com.cotans.driverapp.presenters;


import android.support.v4.widget.SwipeRefreshLayout;

import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.db.Notification;
import com.cotans.driverapp.views.Interfaces.ListOfNotificationsInterface;

import java.util.Collections;
import java.util.List;

import io.reactivex.subscribers.DisposableSubscriber;

public class ListOfNotificationsPresenter implements SwipeRefreshLayout.OnRefreshListener {

    ListOfNotificationsInterface view;

    DatabaseApi databaseApi;

    public ListOfNotificationsPresenter(DatabaseApi databaseApi) {
        this.databaseApi = databaseApi;
    }

    public void bindView(ListOfNotificationsInterface view) {
        this.view = view;
    }

    public void init() {
        getNotifications();
    }

    private void getNotifications() {
        databaseApi.getAllNotifications().subscribe(new DisposableSubscriber<List<Notification>>() {
            @Override
            public void onNext(List<Notification> notifications) {
                Collections.reverse(notifications);
                view.fill(notifications);
            }


            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                dispose();
            }
        });
    }

    @Override
    public void onRefresh() {
        view.stopRefreshDelayed(1);
        getNotifications();
    }
}
