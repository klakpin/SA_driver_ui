package com.cotans.driverapp.views.Interfaces;

import android.content.Context;
import android.os.Bundle;

import com.cotans.driverapp.views.items.OrderProperties;

import java.util.List;

public interface NewOrderScreenInterface {

    void startProgressBar();

    void stopProgressBar();

    void onAcceptButtonPress();

    void fill(List<OrderProperties> properties);

    Bundle getExtras();

    Context getContext();

    void stop();

    void onOrderConfirm();

    void onUnsuccessfulOrderConfirm(String status);
}
