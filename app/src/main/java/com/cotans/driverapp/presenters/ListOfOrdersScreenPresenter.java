package com.cotans.driverapp.presenters;

import com.cotans.driverapp.models.GlobalConstants;
import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.db.Order;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.http.parcelServer.GetDriverDeliveriesResponse;
import com.cotans.driverapp.models.session.SessionManager;
import com.cotans.driverapp.views.Interfaces.ListOfOrdersInterface;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableSingleObserver;


public class ListOfOrdersScreenPresenter {

    private ServerApi serverApi;
    private DatabaseApi db;
    private SessionManager sessionManager;

    private ListOfOrdersInterface view;

    public ListOfOrdersScreenPresenter(ServerApi serverApi, DatabaseApi db, SessionManager sessionManager) {
        this.serverApi = serverApi;
        this.db = db;
        this.sessionManager = sessionManager;
    }

    public void bindView(ListOfOrdersInterface view) {
        this.view = view;
    }

    public void init() {
//        updateOrdersList();
    }

    private void updateOrdersList() {
        view.clearList();
        view.notifyDataChanged();

        serverApi.getDriverDeliveriesSingle(sessionManager.getToken())
                .subscribe(new DisposableSingleObserver<GetDriverDeliveriesResponse>() {
                    @Override
                    public void onSuccess(GetDriverDeliveriesResponse getDriverDeliveriesResponse) {
                        List<Order> orders = getDriverDeliveriesResponse.getResult();
                        List<Order> goodOrders = new ArrayList<>();
                        for (Order ord : orders) {
                            if (ord.getStatus().equals(GlobalConstants.PARCEL_STATUS_CONFIRMED_BY_OPERATOR) ||
                                    ord.getStatus().equals(GlobalConstants.PARCEL_STATUS_PICKED_UP) ||
                                    ord.getStatus().equals(GlobalConstants.PARCEL_STATUS_CONFIRMED_BY_DRIVER)) {
                                goodOrders.add(ord);
                            }
                        }
                        db.insertOrders(goodOrders);
                        db.getAllUndeliveredOrdersFlowable().subscribe(new Consumer<List<Order>>() {
                            @Override
                            public void accept(List<Order> orders) throws Exception {
                                view.fill(goodOrders);
                            }
                        });
                        dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onErrorWhileDownloadingOrders(e.toString());
                        dispose();
                    }
                });
    }

    public void fillInOrders() {
        updateOrdersList();
    }
}
