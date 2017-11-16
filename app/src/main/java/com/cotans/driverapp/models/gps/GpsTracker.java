package com.cotans.driverapp.models.gps;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.cotans.driverapp.models.MyApplication;

public class GpsTracker implements LocationListener {

    private LocationManager manager;


    @SuppressLint("MissingPermission")
    public GpsTracker(Context context) {
        Log.d("Gps tracker", "Initialisation");

        MyApplication.getInstance().connectSocketManager();

        this.manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Gps Tracker", "Sending location " + location.toString());
        MyApplication.getInstance().socketManager.sendMessage(location);
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

    public void stop() {
        manager.removeUpdates(this);
    }
}
