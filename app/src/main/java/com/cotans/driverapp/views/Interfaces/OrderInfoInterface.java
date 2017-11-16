package com.cotans.driverapp.views.Interfaces;

import android.view.View;

import com.cotans.driverapp.views.items.OrderProperties;

import java.util.List;

public interface OrderInfoInterface {


    void startProgressBar();

    void stopProgressBar();

    void fill(List<OrderProperties> properties);

    void setActionButtonClickListener(View.OnClickListener listener);

    void setStartDrivingButtonClickListener(View.OnClickListener listener);

    void startMainActivity(String route, int id);

    void showConfirmationPopUp();

    //TODO unify with other interfaces
    void stop();

    int getDeliveryId();

    void onStartDrivingError(String s);

    void setButtonText(String text);

    void onUnsuccessfulOrderPickup(String status);
}
