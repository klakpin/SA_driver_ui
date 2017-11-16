package com.cotans.driverapp;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.db.Notification;
import com.cotans.driverapp.models.scopes.ContextModule;
import com.cotans.driverapp.models.scopes.DaggerMyApplicationComponent;
import com.cotans.driverapp.models.scopes.MyApplicationComponent;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.subscribers.DisposableSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class NotificationsDatabaseTest {

    MyApplicationComponent component = DaggerMyApplicationComponent
            .builder()
            .contextModule(new ContextModule(InstrumentationRegistry.getTargetContext()))
            .build();

    DatabaseApi databaseApi = component.database();


    @Test
    public synchronized void testWriteRead() throws InterruptedException {
        databaseApi.clearNotificationsTable();

        Notification n1 = new Notification(666, "Hello");
        Notification n2 = new Notification(777, "World");

        databaseApi.insertNotifications(n1, n2);
        wait(50); // Need to be sure that orders are inserted

        databaseApi.getNotificationsNumber().subscribe(integer -> assertThat(integer, is(2)));

        databaseApi.getAllNotifications().subscribe(notifications -> {
            assertThat(notifications.get(0).getText(), is("Hello"));
            assertThat(notifications.get(0).getTimestamp(), is(666L));
            assertThat(notifications.get(1).getText(), is("World"));
            assertThat(notifications.get(1).getTimestamp(), is(777L));
        });
    }

    @Test
    public synchronized void testUpdate() throws InterruptedException {
        databaseApi.clearNotificationsTable();

        Notification n1 = new Notification(666, "Hello");

        databaseApi.insertNotifications(n1);
        wait(100);

        databaseApi.getNotificationsNumber().subscribe(integer -> assertThat(integer, is(1)));

        databaseApi.getAllNotifications().subscribe(notifications -> {
            Notification notification = notifications.get(0);
            notification.setText("text");
            databaseApi.updateNotifications(notification);
        });
        wait(100);
        databaseApi.getAllNotifications().subscribe(notifications -> assertThat(notifications.get(0).getText(), is("text")));
    }

    @Test
    public synchronized void testDeleteNotifications() throws InterruptedException {
        databaseApi.clearNotificationsTable();

        Notification n1 = new Notification(11, "hi");
        Notification n2 = new Notification(22, "ilya");

        databaseApi.insertNotifications(n1, n2);
        wait(100);

        databaseApi.getNotificationsNumber().subscribe(integer -> assertThat(integer, is(2)));

        final Notification[] notification = new Notification[1];

        databaseApi.getAllNotifications().subscribe(new DisposableSubscriber<List<Notification>>() {
            @Override
            public void onNext(List<Notification> notifications) {
                assertThat(notifications.size(), is(2));
                notification[0] = notifications.get(0);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                databaseApi.deleteNotifications(notification[0]).subscribe(integer -> assertThat(integer, is(1)));
                databaseApi.getAllNotifications().subscribe(notifications -> assertThat(notifications.get(0).getText(), is("ilya")));
                dispose();
            }
        });
    }
}

