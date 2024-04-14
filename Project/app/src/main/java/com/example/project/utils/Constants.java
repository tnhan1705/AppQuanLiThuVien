package com.example.project.utils;

public class Constants {
    // main ip
    public static final String SERVER_URL = "ws://192.168.0.109:3500"; // Replace with your server IP or hostname

    // events handlers
    public static final String EVENT_LOGIN = "login";
    public static final String EVENT_GET_DATA = "getData";
    public static final String EVENT_ORDER = "order";
    public  static final String EVENT_ADD_BOOK = "addBook";

    // log type tag
    public static final String TAG_WEBSOCKET = "Websocket";

    public enum TYPE_ALERT{
        OK, OK_CANCEL, TRYAGAIN_CANCEL
    }

    public enum MODE_SUB_TABS{
        HOME,
        NOTE,
    }
}

