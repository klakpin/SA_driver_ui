package com.cotans.driverapp.models.network.http.parcelServer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ParcelServer {

    @GET("/driver/sign/in")
    Call<SignInResponse> signIn(@Query("email") String email, @Query("password") String password);

    @FormUrlEncoded
    @PUT("/driver/log/out")
    Call<LogOutResponce> logOut(@Field("token") String token);

    @GET("/driver/get/deliveries")
    Call<GetDriverDeliveriesResponse> getDeliveries(@Query("token") String token);

    @FormUrlEncoded
    @PUT("/driver/confirm/delivery")
    Call<ConfirmDeliveryResponce> confirmDelivery(@Field("token") String token, @Field("id") int id);

    @FormUrlEncoded
    @PUT("/driver/complete/delivery")
    Call<CompleteDeliveryResponce> completeDelivery(@Field("token") String token, @Field("id") int id, @Field("conf_num") int confirmationNumber);

    @FormUrlEncoded
    @PUT("/driver/pick_up/delivery")
    Call<PickUpDeliveryResponse> pickUpDelivery(@Field("token") String token, @Field("id") int id);

    @GET("/")
    Call<ResponseBody> isApiUp();

    @GET("/driver/emergency/button")
    Call<EmergencyButtonResponce> emergencyButton(@Query("token") String token);


}
