package com.example.project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.DataManager;
import com.example.project.databinding.ActivityLoginBinding; // Import the generated binding class
import com.example.project.entities.DataResponse;
import com.example.project.network.SocketEventListener;
import com.example.project.network.WebSocketClient;
import com.example.project.ui.subFragments.SignUpActivity;
import com.example.project.utils.Constants;
import com.example.project.utils.LoadingDialog;
import com.example.project.utils.PopupUtils;
import com.example.project.utils.UIService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements SocketEventListener {
    // Define a binding variable
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        UIService.HideStatusBar(this, this);

        // Use binding to replace findViewById
        binding.btnSubmit.setOnClickListener(v -> {
            LoadingDialog.getInstance(this).show();

            JSONObject loginObject = new JSONObject();
            try {
                DataManager.getInstance().username = binding.editUsername.getText().toString();

                loginObject.put("event", Constants.EVENT_LOGIN);
                loginObject.put("username", binding.editUsername.getText().toString());
                loginObject.put("password", binding.editPassword.getText().toString());
                String mess = loginObject.toString();

                WebSocketClient.getInstance().requestToServer(mess, this);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        binding.btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onLoginResponse(boolean result) throws JSONException {
        if (result) {
            getAllData();
        } else {
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
        // Implementation not shown for brevity
    }

    void getAllData() throws JSONException {
        JSONObject loginObject = new JSONObject();
        loginObject.put("event", Constants.EVENT_GET_DATA);
        loginObject.put("username", DataManager.getInstance().username);
        String mess = loginObject.toString();
        WebSocketClient.getInstance().requestToServer(mess, this);
    }
}