package com.example.project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.network.SocketEventListener;
import com.example.project.network.WebSocketClient;
import com.example.project.utils.Constants;
import com.example.project.utils.PopupUtils;
import com.example.project.utils.UIService;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements SocketEventListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIService.HideStatusBar(this, this);
        setContentView(R.layout.activity_login);

        // Now you can find the views and set onClickListener
        findViewById(R.id.btnSubmit).setOnClickListener(view -> {
            JSONObject loginObject = new JSONObject();
            try {
                // Corrected casting to EditText
                EditText editUsername = (EditText)findViewById(R.id.editUsername);
                EditText editPassword = (EditText)findViewById(R.id.editPassword);

                loginObject.put("event", Constants.EVENT_LOGIN);
                loginObject.put("username", editUsername.getText().toString());
                loginObject.put("password", editPassword.getText().toString());
                String mess = loginObject.toString();

                WebSocketClient.getInstance().requestToServer(mess, this);
                Toast.makeText(LoginActivity.this, String.format("%s Try to Login", editUsername.getText().toString()), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void onLoginResult(boolean result) {
        if(result){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        else{
            PopupUtils.showPopup(this, "Login Result Notification", "Login attempt failed. Please check your credentials and try again.", Constants.TYPE_ALERT.OK, null, null);
        }
    }
}
