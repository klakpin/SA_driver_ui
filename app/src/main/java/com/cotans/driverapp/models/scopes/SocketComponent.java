package com.cotans.driverapp.models.scopes;


import com.cotans.driverapp.models.network.sockets.SocketManager;
import com.cotans.driverapp.models.network.sockets.SocketModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = SocketModule.class)
public interface SocketComponent {
    SocketManager socketManager();
}
