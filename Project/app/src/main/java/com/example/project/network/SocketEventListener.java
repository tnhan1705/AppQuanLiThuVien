package com.example.project.network;

import org.json.JSONException;
import org.json.JSONObject;

public interface SocketEventListener {
    void onLoginResponse(boolean result) throws JSONException;
    void onGetDataResponse(String data);
}
