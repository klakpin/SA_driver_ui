package com.cotans.driverapp.models.network.http.routeServer;


public class GetConfirmedRouteBody {

    int parcel_id;

    public GetConfirmedRouteBody(int parcel_id) {
        this.parcel_id = parcel_id;
    }

    public int getParcel_id() {
        return parcel_id;
    }
}
