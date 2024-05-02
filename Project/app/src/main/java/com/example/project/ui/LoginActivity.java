package com.example.project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.DataManager;
import com.example.project.R;
import com.example.project.entities.DataResponse;
import com.example.project.network.SocketEventListener;
import com.example.project.network.WebSocketClient;
import com.example.project.utils.Constants;
import com.example.project.utils.ConvertService;
import com.example.project.utils.LoadingDialog;
import com.example.project.utils.PopupUtils;
import com.example.project.utils.UIService;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity implements SocketEventListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIService.HideStatusBar(this, this);
        setContentView(R.layout.activity_login);

        // Now you can find the views and set onClickListener
        findViewById(R.id.btnSubmit).setOnClickListener(view -> {
            LoadingDialog.getInstance(this).show();

            JSONObject loginObject = new JSONObject();
            try {
                // Corrected casting to EditText
                EditText editUsername = (EditText)findViewById(R.id.editUsername);
                EditText editPassword = (EditText)findViewById(R.id.editPassword);

                DataManager.getInstance().username = editUsername.getText().toString();

                loginObject.put("event", Constants.EVENT_LOGIN);
                loginObject.put("username", editUsername.getText().toString());
                loginObject.put("password", editPassword.getText().toString());
                String mess = loginObject.toString();
                Log.d("Client login", "Sending message to server: " + mess);
                WebSocketClient.getInstance().requestToServer(mess, this);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void onLoginResponse(boolean result) throws JSONException {
        if(result){
            GetAllData();
        }
        else{
            LoadingDialog.getInstance(this).hide();
            PopupUtils.showPopup(this, "Login Result Notification", "Login attempt failed. Please check your credentials and try again.", Constants.TYPE_ALERT.OK, null, null);
        }
    }

    @Override
    public void onGetDataResponse(String data) {
        Gson gson = new Gson();
        DataResponse dataResponse = gson.fromJson(data, DataResponse.class);
        DataManager.getInstance().UpdateData(dataResponse);
        LoadingDialog.getInstance(this).hide();
        Log.d("WebSocket", "Received all data success");
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    @Override
    public void onOrderResponse(boolean result) {

    }

    @Override
    public void onHandlePhieu(boolean result) throws JSONException {

    }

    @Override
    public void onHandleUpdate(boolean result) throws JSONException {

    }


    void GetAllData() throws JSONException {
        JSONObject loginObject = new JSONObject();
        loginObject.put("event", Constants.EVENT_GET_DATA);
        loginObject.put("username", DataManager.getInstance().username);
        String mess = loginObject.toString();
        WebSocketClient.getInstance().requestToServer(mess, this);
    }
}
