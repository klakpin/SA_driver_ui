package com.cotans.driverapp.models.network.http.routeServer;


public class CalculateRouteBody {
    String src_address;
    String src_lat;
    String src_lon;
    String dest_address;
    String dest_lat;
    String dest_lon;

    public CalculateRouteBody(String src_address, String src_lat, String src_lon, String dest_address, String dest_lat, String dest_lon) {
        this.src_address = src_address;
        this.src_lat = src_lat;
        this.src_lon = src_lon;
        this.dest_address = dest_address;
        this.dest_lat = dest_lat;
        this.dest_lon = dest_lon;
    }
}
