package com.cotans.driverapp.presenters;

import android.util.Log;

import com.cotans.driverapp.models.GlobalConstants;
import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.db.Order;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.http.parcelServer.PickUpDeliveryResponse;
import com.cotans.driverapp.models.session.SessionManager;
import com.cotans.driverapp.views.Interfaces.OrderInfoInterface;
import com.cotans.driverapp.views.items.OrderProperties;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;
import retrofit2.Response;

public class OrderInfoScreenPresenter {

    private OrderInfoInterface view;

    private int id;
    private DatabaseApi dbApi;
    private ServerApi serverApi;
    private SessionManager sessionManager;

    public OrderInfoScreenPresenter(DatabaseApi db, ServerApi api, SessionManager session) {
        dbApi = db;
        serverApi = api;
        sessionManager = session;
    }

    public void bindView(OrderInfoInterface view) {
        this.view = view;
    }

    public void init() {
        id = view.getDeliveryId();
        view.setStartDrivingButtonClickListener(v -> onStartDrivingButtonPress());
        getOrderInfo(id);
    }

    private void getOrderInfo(int id) {
        dbApi.getOrderMaybe(id).subscribe(this::onFoundOrder);
    }

    private void onFoundOrder(Order order) {
        List<OrderProperties> properties = new ArrayList<>();
        properties.add(new OrderProperties("name", order.getName()));
        properties.add(new OrderProperties("Source", order.getSource()));
        properties.add(new OrderProperties("Destination", order.getDestination()));
        properties.add(new OrderProperties("Urgency", order.getUrgency()));
        properties.add(new OrderProperties("Type", order.getType()));
        properties.add(new OrderProperties("Status", order.getStatus()));
        id = order.getId();

        view.fill(properties);
        Log.d("OrderInfoScree", "Found order " + order.getId() + " is new " + order.isNew() + " status " + order.getStatus());


        switch (order.getStatus()) {
            case "ConfirmedByDriver":
                view.setActionButtonClickListener(v -> onPickUpOrderButtonPressed());
                view.setButtonText("pick up");
                break;
            default:
                view.setActionButtonClickListener(v -> onFinishButtonPressed());
                view.setButtonText("finish");
                break;
        }

    }

    private void onReceivedRoute(String route) {
        dbApi.setRoute(route, id);
        view.stopProgressBar();
        view.startMainActivity(route, id);
    }

    private void onFinishButtonPressed() {
        view.showConfirmationPopUp();
    }

    private void onPickUpOrderButtonPressed() {
        view.startProgressBar();
        String token = sessionManager.getToken();
        serverApi.getOrderPickUpSingle(id, token)
                .subscribe(new DisposableSingleObserver<PickUpDeliveryResponse>() {
                    @Override
                    public void onSuccess(PickUpDeliveryResponse pickUpDeliveryResponse) {
                        onSuccessfulOrderPickup();
                        dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onUnsuccessfulOrderPickup(e.toString());
                        dispose();
                    }
                });
    }

    private void onSuccessfulOrderPickup() {
        dbApi.getOrderMaybe(id).subscribe(new DisposableMaybeObserver<Order>() {
            @Override
            public void onSuccess(Order order) {
                Log.d("OrderInfoPresenter", "Updating order " + id);
                order.setStatus(GlobalConstants.PARCEL_STATUS_CONFIRMED_BY_DRIVER);
                order.setNew(false);
                dbApi.updateOrder(order);
                onFoundOrder(order);
                view.setButtonText("finish");
                view.setActionButtonClickListener(v -> onFinishButtonPressed());
                view.stopProgressBar();
                dispose();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                dispose();
            }
        });
    }

    private void onUnsuccessfulOrderPickup(String status) {
        view.onUnsuccessfulOrderPickup(status);
        view.stopProgressBar();
    }

    private void onRouteReceiveError(Throwable e) {
        view.onStartDrivingError(e.toString());
        view.stopProgressBar();
    }

    private void onStartDrivingButtonPress() {
        view.startProgressBar();
        serverApi.getConfirmedRouteByOrderIdSingle(id)
                .subscribe(new DisposableSingleObserver<Response<String>>() {

                    @Override
                    public void onSuccess(Response<String> s) {
                        onReceivedRoute(s.body());
                    }

                    @Override
                    public void onError(Throwable e) {
                        onRouteReceiveError(e);
                    }
                });
    }
}
