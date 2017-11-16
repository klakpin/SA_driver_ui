package com.cotans.driverapp.models.firebase;

import com.cotans.driverapp.models.MyApplication;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        MyApplication application = MyApplication.getInstance();
        application.sessionManager.setMessagingToken(refreshedToken);
        application.serverApi.sendMessagingToken(application.sessionManager.getEmail(), refreshedToken);
    }
}
