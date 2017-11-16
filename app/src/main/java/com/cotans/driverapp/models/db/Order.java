package com.cotans.driverapp.models.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.widget.ProgressBar;

import com.cotans.driverapp.models.GlobalConstants;

@Entity(tableName = "orders")
public class Order {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "source")
    private String source;
    @ColumnInfo(name = "destination")
    private String destination;
    @ColumnInfo(name = "type")
    private String type;
    @ColumnInfo(name = "urgency")
    private String urgency;
    @ColumnInfo(name = "weight")
    private String weight;
    @ColumnInfo(name = "isDelivered")
    private boolean isDelivered;
    @ColumnInfo(name = "isNew")
    private boolean isNew;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "route")
    private String route;
    //TODO write converter for routes


    @Ignore
    public Order(int id, String name, String source, String destination, String urgency, String type) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.urgency = urgency;
        this.type = type;
        this.isDelivered = false;
    }

    @Ignore
    public Order(int id, String name, String source, String destination) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public boolean isNew() {
        if (status.equals(GlobalConstants.PARCEL_STATUS_CONFIRMED_BY_OPERATOR)) {
            return true;
        } else {
            return false;
        }
    }

    public void setNew(boolean aNew) {
        if (aNew) {
            status = GlobalConstants.PARCEL_STATUS_CONFIRMED_BY_OPERATOR;
        } else {
            status = GlobalConstants.PARCEL_STATUS_CONFIRMED_BY_DRIVER;
        }
    }

    public Order() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }
}
