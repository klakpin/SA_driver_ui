package com.cotans.driverapp.views.Interfaces;


import android.content.Context;

public interface AppStartActivityInterface {

    void startLoading();
    void onSuccess();
    void stop();
    Context getContext();
}
