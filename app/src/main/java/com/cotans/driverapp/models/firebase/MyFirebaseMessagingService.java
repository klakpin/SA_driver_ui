package com.cotans.driverapp.models.firebase;

import android.content.Intent;
import android.util.Log;

import com.cotans.driverapp.models.MyApplication;
import com.cotans.driverapp.models.db.Notification;
import com.cotans.driverapp.models.eventbus.MessageEvent;
import com.cotans.driverapp.views.implementations.NewOrderScreenActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

//TODO refactor
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FireBaseMsgService", "onMessageReceived called, remoteMessage: " + remoteMessage.toString());

        Intent intent = new Intent(this, NewOrderScreenActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);

        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            Log.d("FireBaseMsgService", "entry.getKey() = " + entry.getKey() + "entry.getValue() " + entry.getValue());
            intent.putExtra(entry.getKey(), entry.getValue());
        }

        if (intent.getExtras().containsKey("status")) {
            //
        } else if (intent.getExtras().containsKey("message")) {
            EventBus.getDefault().post(new MessageEvent(intent.getExtras().getString("message")));
            MyApplication.getInstance().db.insertNotifications(new Notification(System.currentTimeMillis() / 1000L, intent.getExtras().getString("message")));
        } else {
            startActivity(intent);
        }
    }
}
