package com.cotans.driverapp.models;


public class GlobalConstants {
    public static final String PARCEL_SERVER_URI = "http://18.220.72.184:4377/";
    public static final String NOTIFICATION_SERVER_URI = "http://18.220.203.246:8080/";
    public static final String ROUTE_SERVER_URI_WEBSOCKET = "ws://18.220.203.246:7080";
    public static final String ROUTE_SERVER_URI_FOR_RETROFIT = "http://18.220.203.246:7080/";
    public static final long GPS_UPDATE_TIME_SECONDS = 1;

    public static final String PARCEL_STATUS_CREATED = "Created";
    public static final String PARCEL_STATUS_CONFIRMED_BY_OPERATOR = "ConfirmedByOperator";
    public static final String PARCEL_STATUS_CONFIRMED_BY_DRIVER = "ConfirmedByDriver";
    public static final String PARCEL_STATUS_PICKED_UP = "PickedUp";
    public static final String PARCEL_STATUS_DELIVERED = "Delivered";
    public static final String PARCEL_STATUS_ARCHIVED = "Archived";
    public static final String PARCEL_STATUS_REJECTED = "Rejected";

}
