package com.cotans.driverapp.models;

import android.app.Application;

import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.gps.GpsTracker;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.sockets.SocketManager;
import com.cotans.driverapp.models.scopes.ContextModule;
import com.cotans.driverapp.models.scopes.DaggerMyApplicationComponent;
import com.cotans.driverapp.models.scopes.DaggerSocketComponent;
import com.cotans.driverapp.models.scopes.MyApplicationComponent;
import com.cotans.driverapp.models.scopes.SocketComponent;
import com.cotans.driverapp.models.session.SessionManager;

import javax.inject.Inject;


public class MyApplication extends Application {

    static private MyApplication instance;


    @Inject
    public DatabaseApi db;

    @Inject
    public ServerApi serverApi;

    @Inject
    public SessionManager sessionManager;

    public SocketManager socketManager;

    public GpsTracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public void connectSocketManager() {
        if (socketManager == null) {
            SocketComponent component = DaggerSocketComponent.builder().build();
            socketManager = component.socketManager();
        }
        socketManager.connect();
    }

    public GpsTracker getTracker() {
        return tracker;
    }

    public void initGps() {
        stopGps();


        tracker = new GpsTracker(this.getApplicationContext());
    }

    public void stopGps() {
        if (tracker != null) {
            tracker.stop();
            tracker = null;
        }
    }

    public static MyApplication getInstance() {
        if (instance.db == null) {
            MyApplicationComponent component = DaggerMyApplicationComponent
                    .builder()
                    .contextModule(new ContextModule(instance.getApplicationContext()))
                    .build();

            component.inject(instance);
        }
        return instance;

    }

    public DatabaseApi getDb() {
        return db;
    }
}
