package com.cotans.driverapp.models.network;

import android.util.Log;

import com.cotans.driverapp.models.network.http.parcelServer.EmergencyButtonResponce;
import com.cotans.driverapp.models.network.http.parcelServer.ParcelServer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class EmergencyAlarmService {

    ParcelServer mParcelServer;
    String token;

    public EmergencyAlarmService(ParcelServer parcelServer) {
        mParcelServer = parcelServer;
    }

    public Observable<String> getAlarmObservable(String token) {

        return Observable.create((ObservableOnSubscribe<String>) e -> {

            Log.d("EmergencyAlarmService", "Sending alarm request");
            Call<EmergencyButtonResponce> call = mParcelServer.emergencyButton(token);
            try {
                Response<EmergencyButtonResponce> response = call.execute();
                e.onNext(response.body().getStatus());
            } catch (IOException e1) {
                e.onNext(e1.toString());
            } finally {
                e.onComplete();
            }

        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .delay(1, TimeUnit.SECONDS)
                .repeat();
    }
}
