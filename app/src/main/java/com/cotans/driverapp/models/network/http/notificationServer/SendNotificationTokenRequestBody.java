package com.cotans.driverapp.models.network.http.notificationServer;


public class SendNotificationTokenRequestBody {
    private String email;
    private String token;

    public SendNotificationTokenRequestBody(String email, String token) {
        this.email = email;
        this.token = token;
    }
}
