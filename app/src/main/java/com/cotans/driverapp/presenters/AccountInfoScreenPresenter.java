package com.cotans.driverapp.presenters;

import android.graphics.Color;

import com.cotans.driverapp.models.MyApplication;
import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.http.parcelServer.LogOutResponce;
import com.cotans.driverapp.models.session.SessionManager;
import com.cotans.driverapp.views.Interfaces.AccountInfoInterface;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableSingleObserver;

public class AccountInfoScreenPresenter {

    private AccountInfoInterface view;


    private ServerApi serverApi;
    private SessionManager sessionManager;
    private DatabaseApi databaseApi;

    private MyApplication myApplicationInstance;

    public AccountInfoScreenPresenter(ServerApi serverApi, SessionManager sessionManager, DatabaseApi databaseApi, MyApplication application) {
        this.serverApi = serverApi;
        this.sessionManager = sessionManager;
        this.databaseApi = databaseApi;
        this.myApplicationInstance = application;
    }

    public void bindView(AccountInfoInterface accountInfoInterface) {
        this.view = accountInfoInterface;
    }

    public void init() {
        setUpGpsStatus();
        drawCharts();
        view.setGpsSwitchChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                myApplicationInstance.initGps();
                view.setGpsSenderStatus(true);
            } else {
                myApplicationInstance.stopGps();
                view.setGpsSenderStatus(false);
            }
        });
        view.setLogoutButtonListener(v -> onLogoutButton());
}

    public void drawCharts() {
        view.setKmData(null);
        view.setFuelConsumptionData(null);

    }

    private void setUpGpsStatus() {
        if (myApplicationInstance.getTracker() == null) {
            view.setGpsSenderStatus(false);
        } else {
            view.setGpsSenderStatus(true);
        }
    }

    public void onLogoutButton() {
        String token = sessionManager.getToken();
        myApplicationInstance.stopGps();
        serverApi.getLogoutSingle(token).subscribe(new DisposableSingleObserver<LogOutResponce>() {
            @Override
            public void onSuccess(LogOutResponce logOutResponce) {
                logout();
            }

            @Override
            public void onError(Throwable e) {
                logout();
            }
        });

    }

    public void logout() {
        sessionManager.logout();
        databaseApi.clearOrdersTable();
        databaseApi.clearNotificationsTable();
        view.startLoginActivity();
    }
}