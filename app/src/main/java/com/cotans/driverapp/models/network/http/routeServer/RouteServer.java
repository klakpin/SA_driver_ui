package com.cotans.driverapp.models.network.http.routeServer;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RouteServer {


    @POST("/routes/getconfirmed")
    Call<String> getConfirmedRoute(@Body RequestBody body);
    //TODO maybe convert it

    @POST("/routes/calculate")
    Call<String> calculateRoute(@Body RequestBody body);
}
