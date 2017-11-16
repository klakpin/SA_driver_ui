package com.cotans.driverapp.models.network.http.parcelServer;


import com.cotans.driverapp.models.db.Order;

import java.util.List;

public class GetDriverDeliveriesResponse {

    private String status;
    private List<Order> result;

    public GetDriverDeliveriesResponse(String status, List<Order> result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getResult() {
        return result;
    }

    public void setResult(List<Order> result) {
        this.result = result;
    }
}
