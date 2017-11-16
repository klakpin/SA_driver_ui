package com.cotans.driverapp.presenters;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.cotans.driverapp.models.DirectionsJSONParser;
import com.cotans.driverapp.models.GlobalConstants;
import com.cotans.driverapp.models.MyApplication;
import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.db.Order;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.http.parcelServer.PickUpDeliveryResponse;
import com.cotans.driverapp.views.Interfaces.MapScreenInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;

public class MapScreenPresenter implements GoogleMap.OnCameraIdleListener, LocationListener {

    MapScreenInterface view;

    private DirectionsJSONParser JsonParser;
    private DatabaseApi databaseApi;
    private ServerApi serverApi;
    private Order currentOrder;
//    private LocationManager manager;

    private GoogleMap mMap;


    public MapScreenPresenter(DirectionsJSONParser jsonParser, DatabaseApi databaseApi, ServerApi serverApi) {
        JsonParser = jsonParser;
        this.databaseApi = databaseApi;
        this.serverApi = serverApi;
    }

    public void bindView(MapScreenInterface view) {
        this.view = view;
    }

    @SuppressLint("MissingPermission")
    public void mapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(this);
        mMap.setMyLocationEnabled(true);
        googleMap.setPadding(0, 1000, 0, 0);
        checkForRoutePayload();
    }

    public void init() {
        view.setEmergencyButtonListener(v -> view.openEmergencyAlertDialog());
//        view.setDriveButtonListener();
    }

    public void checkForRoutePayload() {
        Log.d("MapScreenPresenter", "CheckForRoutePayload view.getOrderId " + view.getOrderId());
        if (view.getOrderId() != -1 && view.getOrderId() != 0) {
            databaseApi.getOrderMaybe(view.getOrderId()).subscribe(new DisposableMaybeObserver<Order>() {
                @Override
                public void onSuccess(Order order) {
                    currentOrder = order;
                    Log.d("MapScreenPresenter", "CurrOrderId " + currentOrder.getId());
                    if (currentOrder.getRoute() != null) {
                        buildRouteForOrder(order);
                    }
                    dispose();
                }

                @Override
                public void onError(Throwable e) {
                    view.showError(e.toString());
                }

                @Override
                public void onComplete() {
                    dispose();
                }
            });
        } else {
            view.hideParcelOverlay();
        }
    }

    private void buildRouteForOrder(Order order) {
        String parse = order.getRoute();
        Log.d("MapScreenPresenter", "Building route for order " + order.getId());
        if (parse == null) {
            view.hideParcelOverlay();
            return;
        }

        Log.d("MapScreenPresenter", "Route for parsing is " + parse);
        JSONObject json;
        try {
            json = new JSONObject(parse);
            List<List<HashMap<String, String>>> result = JsonParser.parse(json);
            drawRoute(result);

            Log.d("MapScreenPresenter", "Set parcel status: " + order.getStatus());
            view.setDestinationOfParcel(currentOrder.getDestination());
            view.setNameOfParcel(currentOrder.getName());

        } catch (JSONException e) {
            e.printStackTrace();
            view.showError(e.toString());
        }

        view.setFinishClickListener(view -> onFinishAction());
        view.setPickUpClickListener(view -> onPickUpAction());
    }

    private void drawRoute(List<List<HashMap<String, String>>> result) {

        mMap.clear();
        ArrayList<LatLng> points;
        PolylineOptions lineOptions;

        points = new ArrayList<>();
        lineOptions = new PolylineOptions();

        List<HashMap<String, String>> path = result.get(0);

        for (int j = 0; j < path.size(); j++) {
            HashMap point = path.get(j);

            double lat = Double.parseDouble((String) point.get("lat"));
            double lng = Double.parseDouble((String) point.get("lng"));
            LatLng position = new LatLng(lat, lng);
            points.add(position);
        }

        lineOptions.addAll(points)
                .width(12)
                .color(Color.BLUE)
                .geodesic(true);

        mMap.addPolyline(lineOptions);

        databaseApi.getOrderMaybe(currentOrder.getId()).subscribe(order -> {
            switch (order.getStatus()) {
                case GlobalConstants.PARCEL_STATUS_CONFIRMED_BY_DRIVER:
                    view.showPickUpButton();
                    view.hideFinishButton();
                    break;
                case GlobalConstants.PARCEL_STATUS_PICKED_UP:
                    view.hidePickUpButton();
                    view.showFinishButton();
                    break;
                default:
                    view.showFinishButton();
            }
        });

        CameraPosition startOfRouteCameraPosition = new CameraPosition
                .Builder()
                .target(points.get(0))
                .zoom(15.5f)
                .tilt(25)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(startOfRouteCameraPosition));

        if (view.getLastRouteOverview() != startOfRouteCameraPosition) {
            Log.d("MapScreenPresenter", "lastRouteOverview != way");
            view.setRouteJustCreated(true);
            view.setLastRouteOverview(startOfRouteCameraPosition);
            view.setLastPos(startOfRouteCameraPosition);
        }
    }

    private void onFinishAction() {
        view.showConfirmationPopup();
    }

    private void onPickUpAction() {
        view.startProgressBar();

        serverApi.getOrderPickUpSingle(currentOrder.getId(), MyApplication.getInstance().sessionManager.getToken())
                .subscribe(new DisposableSingleObserver<PickUpDeliveryResponse>() {
                    @Override
                    public void onSuccess(PickUpDeliveryResponse pickUpDeliveryResponce) {
                        view.hidePickUpButton();
                        view.showFinishButton();
                        databaseApi.updateOrderStatus(currentOrder.getId(), GlobalConstants.PARCEL_STATUS_PICKED_UP);
                        view.notifyUserAboutSuccessfulPickUp();
                        view.stopProgressBar();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onErrorWhilePickUp(e.toString());
                        view.stopProgressBar();
                    }
                });
    }

    @Override
    public void onCameraIdle() {
        //TODO process camera position saving
        if (!view.isRouteJustCreated()) {
            Log.d("MapScreenPresenter", "CameraUpdated");
            view.setRouteJustCreated(false);
            view.setLastPos(mMap.getCameraPosition());
        }
    }

//    private void startLocationTracing() {
//        view.startLocationRequests(this);
//    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void onFinishActivity() {
        mMap.clear();
    }
}
