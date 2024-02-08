package com.example.doanmobile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketClient extends WebSocketListener {
    private static final String SERVER_URL = "ws://192.168.1.13:3500"; // Replace with your server IP or hostname

    private WebSocket webSocket;

    public void start() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS) // Set to 0 to disable timeout
                .build();

        Request request = new Request.Builder()
                .url(SERVER_URL)
                .build();

        webSocket = client.newWebSocket(request, this);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        // WebSocket connection opened
        System.out.println("WebSocket connection opened");
        webSocket.send("Hello, Server!"); // Send a message to the server
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        // Handle received text message
        System.out.println("Received: " + text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
        // Handle received binary message
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        // WebSocket is about to close
        System.out.println("WebSocket is about to close");
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        // WebSocket closed
        System.out.println("WebSocket closed");
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        // WebSocket failure
        System.out.println("WebSocket failure: " + t.getMessage());
        t.printStackTrace();

        if (response != null) {
            System.out.println("Response: " + response.toString());
        }
    }


}
