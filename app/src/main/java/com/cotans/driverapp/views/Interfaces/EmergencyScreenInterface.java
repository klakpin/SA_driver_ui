package com.cotans.driverapp.views.Interfaces;


import android.view.View;

public interface EmergencyScreenInterface {

    void setCancelButtonListener(View.OnClickListener listener);

    void setEsText(String text);

    void cancelAlert();
}
