package com.cotans.driverapp.models.network.http.parcelServer;

import android.support.annotation.Nullable;

public class ConfirmDeliveryResponce {
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Nullable
    public String getResult() {
        return result;
    }

    public void setResult(@Nullable String result) {
        this.result = result;
    }

    @Nullable
    String result;
}

