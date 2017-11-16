package com.cotans.driverapp.views.Interfaces;


import android.content.Context;
import android.view.View;

public interface LoginActivityInterface {

    void askPermissions();

    void startProgressBar();

    void stopProgressBar();

    void onInvalidEmail();

    void onInvalidPassword();

    void onWrongEmailOrPassword();

    String getEmail();

    String getPassword();

    void onUnregistered();

    /**
     * Unknown error while login
     *
     * @param s String with possible error description
     */
    void onLoginFailure(String s);

    void setLoginButtonListener(View.OnClickListener listener);

    Context getContext();

    /**
     * Just call stop() method
     */
    void stop();

    void openMainActivity();
}
