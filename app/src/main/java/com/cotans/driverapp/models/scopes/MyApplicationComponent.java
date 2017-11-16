package com.cotans.driverapp.models.scopes;


import com.cotans.driverapp.models.MyApplication;
import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.db.DatabaseModule;
import com.cotans.driverapp.models.network.http.HttpModule;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.sockets.SocketManager;
import com.cotans.driverapp.models.network.sockets.SocketModule;
import com.cotans.driverapp.models.session.SessionManager;
import com.cotans.driverapp.models.session.SessionManagerModule;

import dagger.Component;

@ApplicationScope
@Component(modules = {DatabaseModule.class, HttpModule.class, SessionManagerModule.class})
public interface MyApplicationComponent {

    void inject(MyApplication myApplication);

    ServerApi serverApi();
    DatabaseApi database();
    SessionManager sessionManager();
}
