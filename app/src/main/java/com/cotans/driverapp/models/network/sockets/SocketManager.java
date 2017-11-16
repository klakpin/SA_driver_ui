package com.cotans.driverapp.models.network.sockets;

import android.location.Location;
import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class SocketManager {


    public boolean isConnected = false;

    WebSocket ws;

    WebSocketAdapter defaultAdapter = new WebSocketAdapter() {
        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            super.onConnected(websocket, headers);
            isConnected = true;
            Log.d("SocketManager", "Connected");
        }

        @Override
        public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
            Log.d("SocketManager", "Sending frame" + frame.toString());
            super.onSendingFrame(websocket, frame);
        }

        @Override
        public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
            Log.d("SocketManager", "Frame sent: " + frame.toString());
            super.onFrameSent(websocket, frame);
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
            isConnected = false;
            Log.d("SocketManager", "Disconnected");
        }
    };

    public SocketManager(WebSocket ws) {
        this.ws = ws;
    }

    public void disconnect() {
        ws.disconnect();
    }

    public void connect() {
        Log.d("SocketManager", "Trying to connect");
        ws.addListener(defaultAdapter);
        ws.connectAsynchronously();
    }

    public void onDisconnect() {
        try {
            Log.d("SocketManager", "Reconnection...");
            ws = ws.recreate();
            ws.clearListeners();
            ws.addListener(defaultAdapter);
            ws.connectAsynchronously();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isUp() {
        Log.d("SocketManager", "Sockets status: " + ws.isOpen());
        return ws.isOpen();
    }

    public void sendMessage(Location loc) {
        String msg = "{\"timestamp\": " + System.currentTimeMillis() / 1000L
                + ", \"lat\": \""
                + loc.getLatitude()
                + "\", \"long\": \""
                + loc.getLongitude()
                + "\"}";

        Log.d("SockeManager", "Sending location " + msg);
        if (isUp()) {
            ws.sendText(msg);
        } else {
            onDisconnect();
            ws.sendText(msg);
        }
    }
}

