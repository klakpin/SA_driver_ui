package com.cotans.driverapp.presenters;

import android.util.Log;

import com.cotans.driverapp.models.MyApplication;
import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.http.parcelServer.CompleteDeliveryResponce;
import com.cotans.driverapp.views.Interfaces.OrderCompletionInterface;

import io.reactivex.observers.DisposableSingleObserver;

public class OrderCompletionScreenPresenter {

    private OrderCompletionInterface view;

    private ServerApi serverApi;
    private DatabaseApi databaseApi;

    public OrderCompletionScreenPresenter(ServerApi serverApi, DatabaseApi databaseApi) {
        this.serverApi = serverApi;
        this.databaseApi = databaseApi;
    }

    public void bindView(OrderCompletionInterface orderCompletionActivity) {
        view = orderCompletionActivity;
    }

    public void onSuccessfulOrderFinish() {
        view.stopProgressBar();
        view.notifyUserAboutSuccessfulOrderFinish();
        databaseApi.markOrderAsDelivered(view.getOrderId());
        view.finishActivity();
    }

    public void onUnsuccessfulOrderCompletion(String message) {
        view.stopProgressBar();
        view.notifyUserAboutNotSuccessfulOrderFinish(message);
    }

    public void onSendCompletionCodeButtonPressed(String inp) {
        view.startProgressBar();
        int input;

        try {
            input = Integer.parseInt(inp);
        } catch (NumberFormatException e) {
            view.stopProgressBar();
            view.onInvalidCodeInput();
            return;
        }
        if (input > 1 && input < 10000) {
            Log.d("OrderCompletion", "Sending completion request with id = " + view.getOrderId() + " and code = " + input);
            serverApi.getCompleteDeliveryRequestSingle(MyApplication.getInstance().sessionManager.getToken(), view.getOrderId(), input)
                    .subscribe(new DisposableSingleObserver<CompleteDeliveryResponce>() {
                        @Override
                        public void onSuccess(CompleteDeliveryResponce completeDeliveryResponce) {
                            if (completeDeliveryResponce.getStatus().equals("ok")) {
                                onSuccessfulOrderFinish();
                            } else {
                                onUnsuccessfulOrderCompletion(completeDeliveryResponce.getStatus());
                            }
                            dispose();
                        }

                        @Override
                        public void onError(Throwable e) {
                            switch (e.getMessage()) {
                                case "wrong_conf_num":
                                    onUnsuccessfulOrderCompletion("Entered code is wrong.");
                                    break;
                                default:
//                                    onUnsuccessfulOrderCompletion(e.toString());
                                    break;
                            }
                            dispose();
                        }
                    });
        } else {
            view.stopProgressBar();
            view.onInvalidCodeInput();
        }
    }
}
