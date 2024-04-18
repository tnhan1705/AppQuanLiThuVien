package com.example.project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.entities.User;
import com.example.project.network.WebSocketClient;
import com.example.project.network.WebSocketResponseListener;
import com.example.project.ui.subFragments.EmailSender;
import com.example.project.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String NUMBERS = "0123456789";

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static final SecureRandom random = new SecureRandom();

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextOtp;

    private EditText editTextConfirmPassword;

    private String currentOtp;
    private String currentEmail;

    public static User currentUser;

    private Boolean resultCheck = new Boolean(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmail = findViewById(R.id.editMail);
        editTextPassword = findViewById(R.id.editPassword);
        editTextOtp = findViewById(R.id.editOtp);
        editTextConfirmPassword = findViewById((R.id.editConfirmPassword));

        findViewById(R.id.btn_forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String otp = editTextOtp.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                if(!confirmPassword.equals((password))){
                    Toast.makeText(ForgotPasswordActivity.this, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(otp.length()<6){
                    Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập mã OTP đủ 6 số!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!otp.equals(currentOtp)||!email.equals(currentEmail)){
                    Toast.makeText(ForgotPasswordActivity.this, "Mã OTP không đúng!", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("event", Constants.EVENT_CHANGE_PASSWORD); // Assuming this is the correct event name for changing a password
                    jsonObject.put("username", currentUser.username);
                    jsonObject.put("newPassword", password);
                    String message = jsonObject.toString();

                    // Send the message via WebSocket to the server
                    WebSocketClient.getInstance().send(message);

                    // Inform the user about successful password change
                    Toast.makeText(ForgotPasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

                    // Redirect the user to the login activity
                    Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.sendOtp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentOtp = generateRandomOTP(6);
                String email = editTextEmail.getText().toString().trim();
                if(!email.equals(currentUser.email)){
                    Toast.makeText(ForgotPasswordActivity.this, "Email không chính xác!", Toast.LENGTH_SHORT).show();
                    return;
                }
                currentEmail = currentUser.email;
                startCountDown();
                EmailSender.sendOTP(currentEmail, currentOtp, ForgotPasswordActivity.this);
            }
        });
    }
    private static String generateRandomOTP(int length) {
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(NUMBERS.length());
            char randomChar = NUMBERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }
    private void startCountDown() {
        TextView sendOtpTextView = findViewById(R.id.sendOtp);
        sendOtpTextView.setEnabled(false);

        new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Cập nhật văn bản của TextView để hiển thị đếm ngược
                sendOtpTextView.setText("Gửi lại sau " + millisUntilFinished / 1000 + " giây");
            }

            @Override
            public void onFinish() {
                // Đếm ngược kết thúc, cập nhật văn bản và kích hoạt lại TextView
                sendOtpTextView.setText("Send");
                sendOtpTextView.setEnabled(true);
            }
        }.start();

        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                currentOtp = generateRandomOTP(6);
            }
        }.start();
    }
    public boolean validateEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
                    if(!data.equals("flase")){
                        resultCheck = true;
                    }
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}