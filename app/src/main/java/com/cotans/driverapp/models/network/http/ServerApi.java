package com.cotans.driverapp.models.network.http;


import android.support.annotation.NonNull;
import android.util.Log;

import com.cotans.driverapp.models.network.http.notificationServer.NotificationServer;
import com.cotans.driverapp.models.network.http.notificationServer.SendNotificationTokenRequestBody;
import com.cotans.driverapp.models.network.http.parcelServer.CompleteDeliveryResponce;
import com.cotans.driverapp.models.network.http.parcelServer.ConfirmDeliveryResponce;
import com.cotans.driverapp.models.network.http.parcelServer.EmergencyButtonResponce;
import com.cotans.driverapp.models.network.http.parcelServer.GetDriverDeliveriesResponse;
import com.cotans.driverapp.models.network.http.parcelServer.LogOutResponce;
import com.cotans.driverapp.models.network.http.parcelServer.ParcelServer;
import com.cotans.driverapp.models.network.http.parcelServer.PickUpDeliveryResponse;
import com.cotans.driverapp.models.network.http.parcelServer.SignInResponse;
import com.cotans.driverapp.models.network.http.routeServer.RouteServer;

import java.io.IOException;
import java.util.concurrent.Executor;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ServerApi {

    private RouteServer mRouteServer;
    private NotificationServer mNotificationServer;
    public ParcelServer mParcelServer;
    private Executor executor;

    public ServerApi(RouteServer mRouteServer, NotificationServer mNotificationServer, ParcelServer mParcelServer, Executor executor) {
        this.mRouteServer = mRouteServer;
        this.mNotificationServer = mNotificationServer;
        this.mParcelServer = mParcelServer;
        this.executor = executor;
    }

    public Single<PickUpDeliveryResponse> getOrderPickUpSingle(int id, String token) {
        return Single.create((SingleOnSubscribe<PickUpDeliveryResponse>) observer -> {
            Call<PickUpDeliveryResponse> call = mParcelServer.pickUpDelivery(token, id);
            try {
                Response<PickUpDeliveryResponse> response = call.execute();
                if (response.body().getStatus().equals("ok")) {
                    observer.onSuccess(response.body());
                } else {
                    observer.onError(new Throwable(response.body().getStatus()));
                }
            } catch (IOException e) {
                e.printStackTrace();
                observer.onError(e);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<GetDriverDeliveriesResponse> getDriverDeliveriesSingle(String token) {
        return Single.create((SingleOnSubscribe<GetDriverDeliveriesResponse>) observer -> {
            try {
                Call<GetDriverDeliveriesResponse> call = mParcelServer.getDeliveries(token);
                Response<GetDriverDeliveriesResponse> response = call.execute();
                if (response.body().getStatus().equals("ok")) {
                    observer.onSuccess(response.body());
                } else {
                    observer.onError(new Throwable(response.body().getStatus()));
                }
            } catch (IOException e) {
                e.printStackTrace();
                observer.onError(e);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public Single<Response<String>> getConfirmedRouteByOrderIdSingle(int id) {
        return Single.create((SingleOnSubscribe<Response<String>>) e -> {
            try {
                String bodyStr = "{\"parcel_id\":" + id + "}";
                Log.d("ServerApi", "Request body " + bodyStr);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), bodyStr);
                Call<String> call = mRouteServer.getConfirmedRoute(requestBody);
                Response<String> response = call.execute();
                if (response.body() != null) {
                    e.onSuccess(response);
                } else {
                    e.onError(new Throwable("Route not found."));
                }
            } catch (IOException ex) {
                e.onError(ex);
                ex.printStackTrace();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }


    @NonNull
    private RequestBody parseCalculateRouteBody(String src_address, String src_lat, String src_lon,
                                                String dest_address, String dest_lat, String dest_lon) {
        String bodyString = "{\n" +
                "\t\"src_address\": \" " + src_address + " \"\n" +
                "\t\"src_lat\": \" " + src_lat + " \"\n" +
                "\t\"src_lon\": \" " + src_lon + " \"\n" +
                "\t\"dest_address\": \" " + dest_address + " \"\n" +
                "\t\"dest_lat\": \" " + dest_lat + " \"\n" +
                "\t\"dest_lon\": \" " + dest_lon + " \"\n" +
                "}";
        return RequestBody.create(MediaType.parse("application/json"), bodyString);
    }

    public Single<String> getRouteSingle(String src_address, String src_lat, String src_lon,
                                         String dest_address, String dest_lat, String dest_lon) {
        return Single.create((SingleOnSubscribe<String>) observer -> {
            try {
                Call<String> call = mRouteServer.calculateRoute(parseCalculateRouteBody(src_address, src_lat, src_lon, dest_address, dest_lat, dest_lon));
                Response<String> response = call.execute();
                if (response.body() != null) {
                    observer.onSuccess(response.body());
                } else {
                    Log.d("onError", "route not found when sending " + src_address);
                    observer.onError(new Throwable("Route not found"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                observer.onError(e);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

    }

    public Single<Response<ResponseBody>> sendMessagingToken(String email, String token) {
        return Single.create((SingleOnSubscribe<Response<ResponseBody>>) observer -> {
            try {
                Call<ResponseBody> call = mNotificationServer.sendNotificationToken(new SendNotificationTokenRequestBody(email, token));
                Response<ResponseBody> responseBody = call.execute();
                if (responseBody.code() == 200) {
                    observer.onSuccess(responseBody);
                } else {
                    observer.onError(new Throwable("Error while sending firebase token, " + responseBody.toString()));
                }
            } catch (IOException e) {
                e.printStackTrace();
                observer.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<SignInResponse> getSendLoginRequestSingle(String email, String password) {
        return Single.create((SingleOnSubscribe<SignInResponse>) observer -> {
            try {
                Call<SignInResponse> call = mParcelServer.signIn(email, password);
                Response<SignInResponse> result = call.execute();
                if (result.body().getStatus().equals("ok")) {
                    observer.onSuccess(result.body());
                } else {
                    observer.onError(new Throwable(result.body().getStatus()));
                }
            } catch (IOException e) {
                observer.onError(e);
                e.printStackTrace();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<LogOutResponce> getLogoutSingle(String token) {
        return Single.create((SingleOnSubscribe<LogOutResponce>) observer -> {
            try {
                Call<LogOutResponce> call = mParcelServer.logOut(token);
                Response<LogOutResponce> result = call.execute();
                if (result.body().getStatus().equals("ok")) {
                    observer.onSuccess(result.body());
                } else {
                    observer.onError(new Throwable(result.body().toString()));
                }
            } catch (IOException e) {
                e.printStackTrace();
                observer.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ConfirmDeliveryResponce> getOrderConfirmRequestSingle(String token, int id) {
        return Single.create((SingleOnSubscribe<ConfirmDeliveryResponce>) observer -> {
            try {
                Call<ConfirmDeliveryResponce> call = mParcelServer.confirmDelivery(token, id);
                Response<ConfirmDeliveryResponce> result = call.execute();
                if (result.body().getStatus().equals("ok")) {
                    observer.onSuccess(result.body());
                } else {
                    observer.onError(new Throwable(result.body().getStatus()));
                }
            } catch (IOException e) {
                e.printStackTrace();
                observer.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<CompleteDeliveryResponce> getCompleteDeliveryRequestSingle(String token, int id, int confirmationCode) {
        return Single.create((SingleOnSubscribe<CompleteDeliveryResponce>) observer -> {
            try {
                Call<CompleteDeliveryResponce> call = mParcelServer.completeDelivery(token, id, confirmationCode);
                Response<CompleteDeliveryResponce> result = call.execute();
                if (result.body().getStatus().equals("ok")) {
                    observer.onSuccess(result.body());
                } else {
                    observer.onError(new Throwable(result.body().getStatus()));
                }
            } catch (IOException e) {
                e.printStackTrace();
                observer.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Completable sendEmergencySignal(String token) {
        return Completable.create(observer -> {
            Call<EmergencyButtonResponce> call = mParcelServer.emergencyButton(token);
            try {
                Response<EmergencyButtonResponce> result = call.execute();
                if (result.body().getStatus().equals("ok")) {
                    observer.onComplete();
                } else {
                    observer.onError(new Exception(result.body().getStatus()));
                }
            } catch (IOException e) {
                observer.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
