package com.cotans.driverapp.models.scopes;


import com.cotans.driverapp.models.MyApplication;
import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.sockets.SocketManager;
import com.cotans.driverapp.models.session.SessionManager;

import dagger.Module;
import dagger.Provides;

@Module
@ActivityScope
public class MyApplicationModule {

    @Provides
    @ActivityScope
    ServerApi serverApi() {
        return MyApplication.getInstance().serverApi;
    }

    @Provides
    @ActivityScope
    DatabaseApi databaseApi() {
        return MyApplication.getInstance().db;
    }

    @Provides
    @ActivityScope
    SocketManager socketManager() {
        return MyApplication.getInstance().socketManager;
    }

    @Provides
    @ActivityScope
    SessionManager sessionManager() {
        return MyApplication.getInstance().sessionManager;
    }
}
