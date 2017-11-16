package com.cotans.driverapp.views.Interfaces;

public interface OrderCompletionInterface {

    void onSendConfirmationCodeButtonPress(String input);

    void onInvalidCodeInput();

    void stop();

    void startProgressBar();

    void stopProgressBar();

    void notifyUserAboutSuccessfulOrderFinish();

    void finishActivity();

    void notifyUserAboutNotSuccessfulOrderFinish(String message);

    int getOrderId();
}
