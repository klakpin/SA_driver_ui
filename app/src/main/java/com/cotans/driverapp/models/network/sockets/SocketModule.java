package com.cotans.driverapp.models.network.sockets;

import android.util.Log;

import com.cotans.driverapp.models.scopes.ApplicationScope;
import com.cotans.driverapp.models.GlobalConstants;
import com.cotans.driverapp.models.MyApplication;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Singleton
@Module
public class SocketModule {

    @Singleton
    @Provides
    SocketManager socketManager(WebSocket ws) {
        if (ws != null) {
            return new SocketManager(ws);
        } else {
            return null;
        }
    }

    @Singleton
    @Provides
    WebSocket webSocket(WebSocketFactory factory) {
        try {
            Log.d("SocketModule", "Connecting to socket " + GlobalConstants.ROUTE_SERVER_URI_WEBSOCKET + "/driver/send_location?driver_token=" + MyApplication.getInstance().sessionManager.getToken());
            WebSocket result = factory.createSocket(GlobalConstants.ROUTE_SERVER_URI_WEBSOCKET + "/driver/send_location?driver_token=" + MyApplication.getInstance().sessionManager.getToken());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Singleton
    @Provides
    WebSocketFactory webSocketFactory() {
        return new WebSocketFactory();
    }
}

