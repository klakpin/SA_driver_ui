package com.cotans.driverapp.presenters;

import android.os.Bundle;

import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.db.Order;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.http.parcelServer.ConfirmDeliveryResponce;
import com.cotans.driverapp.models.session.SessionManager;
import com.cotans.driverapp.views.Interfaces.NewOrderScreenInterface;
import com.cotans.driverapp.views.items.OrderProperties;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableSingleObserver;

public class NewOrderScreenPresenter {

    NewOrderScreenInterface view;

    ServerApi serverApi;
    DatabaseApi databaseApi;
    SessionManager sessionManager;

    public NewOrderScreenPresenter(ServerApi serverApi, DatabaseApi databaseApi, SessionManager sessionManager) {
        this.serverApi = serverApi;
        this.databaseApi = databaseApi;
        this.sessionManager = sessionManager;
    }

    Order currentOrder;

    public void bindView(NewOrderScreenInterface newOrderScreenActivity) {
        view = newOrderScreenActivity;
    }

    public void init() {

        if (view.getExtras().containsKey("FROM_ADAPTER") && view.getExtras().getBoolean("FROM_ADAPTER")) {
            // Activity created from listview adapter
            getOrderInfo(view.getExtras().getInt("id"));
        } else {
            // Activity created from firebase service
            currentOrder = convertExtrasToOrder(view.getExtras());
            addOrderToDatabase(currentOrder);
            view.fill(convertOrderToListOfProperties(currentOrder));
        }
    }

    public void getOrderInfo(int id) {
        databaseApi.getOrderMaybe(id).subscribe(order -> onFoundOrder(order));
    }

    public void onFoundOrder(Order order) {
        this.currentOrder = order;
        List<OrderProperties> properties = new ArrayList<>();
        properties.add(new OrderProperties("name", order.getName()));
        properties.add(new OrderProperties("Source", order.getSource()));
        properties.add(new OrderProperties("Destination", order.getDestination()));
        properties.add(new OrderProperties("Urgency", order.getUrgency()));
        properties.add(new OrderProperties("Type", order.getType()));
        view.fill(properties);
    }


    private Order convertExtrasToOrder(Bundle extras) {
        Order order = new Order();

        if (extras.containsKey("id")) {
            order.setId(Integer.parseInt(extras.getString("id")));
        }
        if (extras.containsKey("name")) {
            order.setName(extras.getString("name"));
        }
        if (extras.containsKey("source")) {
            order.setSource(extras.getString("source"));
        }
        if (extras.containsKey("destination")) {
            order.setDestination(extras.getString("destination"));
        }
        if (extras.containsKey("urgency")) {
            order.setUrgency(extras.getString("urgency"));
        }
        if (extras.containsKey("type")) {
            order.setType(extras.getString("type"));
        }
        order.setNew(true);

        return order;
    }

    private void addOrderToDatabase(Order order) {
        databaseApi.insertOrders(order);
    }

    public List<OrderProperties> convertOrderToListOfProperties(Order order) {
        List<OrderProperties> properties = new ArrayList<>();
        properties.add(new OrderProperties("name", order.getName()));
        properties.add(new OrderProperties("source", order.getSource()));
        properties.add(new OrderProperties("destination", order.getDestination()));
        properties.add(new OrderProperties("urgency", order.getUrgency()));
        properties.add(new OrderProperties("type", order.getType()));
        return properties;
    }

    public void onAcceptButtonPressed() {
        view.startProgressBar();
        String token = sessionManager.getToken();
        serverApi.getOrderConfirmRequestSingle(token, currentOrder.getId())
                .subscribe(new DisposableSingleObserver<ConfirmDeliveryResponce>() {
                    @Override
                    public void onSuccess(ConfirmDeliveryResponce confirmDeliveryResponce) {
                        onSuccessfulOrderConfirm();
                        dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onUnsuccessfulOrderConfirm(e.toString());
                        dispose();
                    }
                });
    }

    public void onSuccessfulOrderConfirm() {
        currentOrder.setNew(false);
        databaseApi.updateOrder(currentOrder);
        view.onOrderConfirm();
        view.stop();
    }

    public void onUnsuccessfulOrderConfirm(String status) {
        view.stopProgressBar();
        view.onUnsuccessfulOrderConfirm(status);
    }
}
