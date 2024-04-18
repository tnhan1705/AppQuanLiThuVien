package com.example.project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.DataManager;
import com.example.project.R;
import com.example.project.entities.DataResponse;
import com.example.project.entities.User;
import com.example.project.network.SocketEventListener;
import com.example.project.network.WebSocketClient;
import com.example.project.network.WebSocketResponseListener;
import com.example.project.ui.subFragments.SignUpActivity;
import com.example.project.utils.Constants;
import com.example.project.utils.ConvertService;
import com.example.project.utils.LoadingDialog;
import com.example.project.utils.PopupUtils;
import com.example.project.utils.UIService;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity implements SocketEventListener {
    TextView signUpTxt;
    private Boolean resultCheck = new Boolean(true);
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

                WebSocketClient.getInstance().requestToServer(mess, this);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!resultCheck) {
                    Toast.makeText(LoginActivity.this, "Username không tồn tại!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent;
                intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        EditText editTextUsername = (EditText)findViewById(R.id.editUsername);
        editTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String username = editTextUsername.getText().toString().trim();
                    checkUserName(username);
                }
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
    private void checkUserName(String userName) {
        Gson gson = new Gson();
        String userJson = gson.toJson(userName);
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("event", Constants.EVENT_CHECK_USERNAME);
            jsonObject.put("username", userName);
            String message = jsonObject.toString();

            // Gửi tin nhắn và gắn listener
            WebSocketClient.getInstance().send(message, new WebSocketResponseListener() {
                @Override
                public void checkUserNameResponse(String data) {
                    // Xử lý phản hồi từ máy chủ
                    if(data.equals("null")){
                        resultCheck = false;
                    }
                    else {
                        resultCheck = true;
                        DataManager.getInstance().getUser(data);
                        ForgotPasswordActivity.currentUser = DataManager.getInstance().getUser();
                    }
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    void GetAllData() throws JSONException {
        JSONObject loginObject = new JSONObject();
        loginObject.put("event", Constants.EVENT_GET_DATA);
        loginObject.put("username", DataManager.getInstance().username);
        String mess = loginObject.toString();
        WebSocketClient.getInstance().requestToServer(mess, this);
    }
}
