package com.cotans.driverapp.models.network.http.notificationServer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificationServer {

    @POST("/driver/register")
    Call<ResponseBody> sendNotificationToken(@Body SendNotificationTokenRequestBody body);
}
