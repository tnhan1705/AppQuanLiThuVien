package com.example.project.utils;

public class Constants {
    // main ip
    public static final String SERVER_URL = "ws://192.168.0.102:3500"; // Replace with your server IP or hostname

    // events handlers
    public static final String EVENT_LOGIN = "login";

    // log type tag
    public static final String TAG_WEBSOCKET = "Websocket";

    public enum TYPE_ALERT{
        OK, OK_CANCEL, TRYAGAIN_CANCEL
    }
}

