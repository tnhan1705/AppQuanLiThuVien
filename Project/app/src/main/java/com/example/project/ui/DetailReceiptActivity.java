package com.example.project.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.project.DataManager;
import com.example.project.R;
import com.example.project.entities.Book;
import com.example.project.entities.DataResponse;
import com.example.project.entities.Receipt;
import com.example.project.network.SocketEventListener;
import com.example.project.network.WebSocketClient;
import com.example.project.ui.custom_adapter.CustomReceiptAdapter;
import com.example.project.ui.subFragments.FragmentBorrowing;
import com.example.project.utils.Constants;
import com.example.project.utils.CreateService;
import com.example.project.utils.LoadingDialog;
import com.example.project.utils.PopupUtils;
import com.example.project.utils.UIService;
import com.google.gson.Gson;
import android.util.Log;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import android.view.LayoutInflater;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.view.View;
import android.widget.Button;



public class DetailReceiptActivity extends AppCompatActivity implements  SocketEventListener {

    EditText editDateFrom;
    EditText editDateTo;

    CustomReceiptAdapter  adapter ;



    ListView listView;
    View view;
     Receipt receipt = null;
    private Context FragmentReturned;

    public DetailReceiptActivity() {

    }

    @SuppressLint({"RestrictedApi", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIService.HideStatusBar(this, this);
        setContentView(R.layout.activity_detail_receipt);


//        Log.d("FragmentLifecycle", "Fra
        Intent intent = getIntent();

        Receipt receipts = (Receipt) intent.getSerializableExtra("receipt");

        // Hiển thị dữ liệu của Receipt lên EditText
        EditText editTextFirstName = findViewById(R.id.edit_First_Name);
        EditText editTextLastName = findViewById(R.id.edit_Last_Name);
        RadioButton radioButtonMale = findViewById(R.id.maleRadioButton);
        RadioButton radioButtonFemale = findViewById(R.id.femaleRadioButton);
        RadioButton radioButtonNonBinary = findViewById(R.id.nonBinaryRadioButton);
        EditText editTContactEmail = findViewById(R.id.edit_Contact_Email);
        EditText editContactPhone = findViewById(R.id.editContactPhone);
        EditText editDateForm = findViewById(R.id.editDateFrom);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String startDate = dateFormat.format(receipts.getDate_start());
        EditText editDateTo = findViewById(R.id.editDateTo);
        SimpleDateFormat dateTo = new SimpleDateFormat("dd/MM/yyyy");
        String ReturnDate = dateFormat.format(receipts.getDate_return());

            editTextFirstName.setText(receipts.getFirst_name());
            editTextLastName.setText(receipts.getLast_name());
            editTContactEmail.setText(receipts.getEmail());
            editContactPhone.setText(receipts.getPhone());
            editDateForm.setText( startDate);
            editDateTo.setText( ReturnDate);


            String gender = receipts.getGender();

            if (gender.equals("Male")) {
                radioButtonMale.setChecked(true);
            } else if (gender.equals("Female")) {
                radioButtonFemale.setChecked(true);
            } else {
                radioButtonNonBinary.setChecked(true);
            }



        ImageButton btnback = findViewById(R.id.btnBack);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Nhận Intent từ hoạt động trước

        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnsubmit = findViewById(R.id.btnUpdate);
        Button  btnRemark = findViewById(R.id.btnMarkReturn);

        // Kiểm tra Intent có chứa extra "hideCancel" không
        boolean hideCancel = getIntent().getBooleanExtra("hideCancel", false);
        Log.d("DetailReceiptActivity", "hideCancel: loi " + hideCancel);

        if (hideCancel) {
            // Ẩn btnCancel nếu cần
            btnCancel.setVisibility(View.GONE);
            btnsubmit.setVisibility(View.GONE);
            btnRemark.setVisibility(View.GONE);

        }

        LinearLayout container = findViewById(R.id.container_items_book);
        LayoutInflater inflater = LayoutInflater.from(this); // Tạo LayoutInflater
// hiện  các sach có cùng id
        Button btn_update = findViewById(R.id.btnUpdate);

        btn_update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    onClickUpdate();
                    Log.e("YourTag", "ParseException occurred");
                } catch (ParseException e) {
                    Log.e("YourTag", "ParseException occurred", e);
                    throw new RuntimeException(e);
                }
            }
        });

        // Kiểm tra xem Intent có dữ liệu không
        if (intent.hasExtra("receipt")) {
            Gson gson = new Gson();
            String json = intent.getStringExtra("receipt");
            Receipt receipt = gson.fromJson(json, Receipt.class);
            Book[] books = receipt.getBooksByIDs();
            Log.d("Receipt Information", "Receipt Status: " + receipt.getStatus());

            for (int i = 0; i < books.length; i++) { // Số lượng item bạn muốn chèn
                View itemView = inflater.inflate(R.layout.list_item_book_review, null); // Inflate layout của mỗi item
                // Thực hiện các thay đổi cần thiết trên itemView, như đặt dữ liệu, sự kiện, v.v.
                // Ví dụ:
                TextView name_book = itemView.findViewById(R.id.name_book);
                TextView author_name = itemView.findViewById(R.id.author_name);
                TextView summary = itemView.findViewById(R.id.summary);

                name_book.setText(books[i].name);
                author_name.setText(books[i].name_author);
                summary.setText((books[i].summary));

                ImageView imageView = itemView.findViewById(R.id.image_book);
                byte[] decodedString = Base64.decode(books[i].image, Base64.DEFAULT);
                if (decodedString != null && decodedString.length > 0){
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    books[i].decodedByte = decodedByte;
                    imageView.setImageBitmap(decodedByte);
                }

                // Thêm itemView vào LinearLayout
                container.addView(itemView);

                EditText edit_first_name = findViewById(R.id.edit_first_name);
                edit_first_name.setText(receipt.first_name);

                EditText edit_last_name = findViewById(R.id.edit_last_name);
                edit_last_name.setText(receipt.last_name);

                RadioButton maleRadioButton = findViewById(R.id.maleRadioButton);
                maleRadioButton.setChecked(receipt.gender.equals(maleRadioButton.getText()));
                RadioButton femaleRadioButton = findViewById(R.id.femaleRadioButton);
                femaleRadioButton.setChecked(receipt.gender.equals(femaleRadioButton.getText()));
                RadioButton nonBinaryRadioButton = findViewById(R.id.nonBinaryRadioButton);
                nonBinaryRadioButton.setChecked(receipt.gender.equals(nonBinaryRadioButton.getText()));

                TextView edit_email = findViewById(R.id.edit_email);
                edit_email.setText(receipt.email);

                TextView edit_region_number = findViewById(R.id.edit_region_number);
                edit_region_number.setText("+" + receipt.phone.substring(1, 3));

                TextView edit_phone = findViewById(R.id.edit_phone);
                edit_phone.setText(receipt.phone.substring(3));

                editDateFrom = findViewById(R.id.editDateFrom);
                editDateFrom.setEnabled(false);
                editDateFrom.setBackgroundResource(R.drawable.ip_disable);
                editDateFrom.setText(receipt.date_start.toString());

                editDateTo = findViewById((R.id.editDateTo));
                editDateTo.setText(receipt.date_return.toString());
            }
        }


        ImageView btnCalendar = findViewById(R.id.btnDateTo);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        findViewById(R.id.btnMarkReturn).setOnClickListener(view -> {
            // Hiển thị loading dialog để biểu thị rằng yêu cầu đang được xử lý
            LoadingDialog.getInstance(this).show();
            Gson gson = new Gson();
            Receipt receipt = new Receipt();
            receipt.status = "Return";
            receipt.id = receipts.getId();
            this.receipt = receipt;
            System.out.println("187  "+ this.receipt.getStatus());


            // Tạo một đối tượng JSONObject để chứa dữ liệu yêu cầu
            JSONObject remarkObject = new JSONObject();
            try {
                // Thêm các trường dữ liệu vào JSONObject

                remarkObject.put("event", Constants.EVENT_REMARK); // Xác định sự kiện
                remarkObject.put("receipt", gson.toJson(this.receipt));
                remarkObject.put("username", DataManager.getInstance().username);

                // Chuyển đổi JSONObject thành chuỗi JSON
                String message = remarkObject.toString();
                Log.d("Client", "Sending message to server: " + message);
                // Gửi yêu cầu cập nhật lên máy chủ thông qua kết nối WebSocket
                WebSocketClient.getInstance().requestToServer(message,  this);

            } catch (JSONException e) {
                // Xử lý nếu có lỗi khi tạo JSONObject
                throw new RuntimeException(e);
            }
        });

        findViewById(R.id.btnCancel).setOnClickListener(view -> {
            // Hiển thị loading dialog để biểu thị rằng yêu cầu đang được xử lý
            LoadingDialog.getInstance(this).show();
            Gson gson = new Gson();
            Receipt receipt = new Receipt();
            receipt.status = "Cancel";
            receipt.id = receipts.getId();
            this.receipt = receipt;
            System.out.println("187  "+ this.receipt.getStatus());

            // Tạo một đối tượng JSONObject để chứa dữ liệu yêu cầu
            JSONObject remarkObject = new JSONObject();
            try {
                // Thêm các trường dữ liệu vào JSONObject

                remarkObject.put("event", Constants.EVENT_REMARK); // Xác định sự kiện
                remarkObject.put("receipt", gson.toJson(receipt));
                remarkObject.put("username", DataManager.getInstance().username);

                // Chuyển đổi JSONObject thành chuỗi JSON
                String message = remarkObject.toString();
                Log.d("Client", "Sending message to server: " + message);
                // Gửi yêu cầu cập nhật lên máy chủ thông qua kết nối WebSocket
                WebSocketClient.getInstance().requestToServer(message,  this);
            } catch (JSONException e) {
                // Xử lý nếu có lỗi khi tạo JSONObject
                throw new RuntimeException(e);
            }
        });
    }

    public void showDatePickerDialog(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String formattedDate = String.format("%04d/%02d/%02d", year, monthOfYear + 1, dayOfMonth);
                        editDateTo.setText(formattedDate);
                    }
                }, year, month, day);

        datePickerDialog.show();
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
    }



    @Override
    public void onOrderResponse(boolean result) throws JSONException {

    }

    public void onHandlePhieu(boolean result) throws JSONException {
        System.out.println("Detail "+ result);
        if(result ) {
            GetAllData();
            PopupUtils.showPopup(this, "Notify", "The status has been successfully updated.", Constants.TYPE_ALERT.OK, new PopupUtils.OnClickListener() {
                @Override
                public void OnPositiveClicked() {
                    finish();
                }

                @Override
                public void OnNegativeClicked() {

                }
            }, null);
        }
        else {
            PopupUtils.showPopup(this, "AAAAWarning", "The status update failed. Please try again", Constants.TYPE_ALERT.OK, null, null);
            LoadingDialog.getInstance(this).hide();
        }
    }



    public void onClickUpdate() throws ParseException {
        LoadingDialog.getInstance(this).show();
        Log.e("Update click", "ParseException occurred");
        Intent intent = getIntent();
        Receipt receipts = (Receipt) intent.getSerializableExtra("receipt");
        Gson gson = new Gson();

        Receipt receipt = new Receipt();

        EditText edit_first_name = findViewById(R.id.edit_First_Name);
        receipt.first_name = edit_first_name.getText().toString();

        EditText edit_last_name = findViewById(R.id.edit_Last_Name);
        receipt.last_name = edit_last_name.getText().toString();

        RadioGroup genderRadioGroup = findViewById(R.id.genderRadioGroup);
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedId);
        String selectedGender = selectedRadioButton.getText().toString();
        receipt.gender = selectedGender;

        EditText edit_email = findViewById(R.id.edit_Contact_Email);
        receipt.email = edit_email.getText().toString();

        EditText edit_phone1 = findViewById(R.id.editContactPhone);

        receipt.phone = edit_phone1.getText().toString()  ;

        receipt.id = receipts.getId();

        this.receipt = receipt;

        JSONObject loginObject = new JSONObject();
        try {
            loginObject.put("event", Constants.EVENT_UPDATE);
            loginObject.put("receipt", gson.toJson(receipt));
            loginObject.put("username", DataManager.getInstance().username);
            String mess = loginObject.toString();
            Log.d("Client", "Sending message to server: " + Constants.EVENT_UPDATE);
            Log.d("Client", "Sending message to server: " + mess);
            WebSocketClient.getInstance().requestToServer(mess, this);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    public void onHandleUpdate(boolean result) throws JSONException {
        System.out.println("Update "+ result);

        if(result ) {
            GetAllData();
            PopupUtils.showPopup(this, "Notify", "The loan form has been successfully updatedl.", Constants.TYPE_ALERT.OK, new PopupUtils.OnClickListener() {
                @Override
                public void OnPositiveClicked() {
                    finish();
                }

                @Override
                public void OnNegativeClicked() {

                }
            }, null);
        }
        else {
            PopupUtils.showPopup(this, "Warning", "Update failed. Please try again", Constants.TYPE_ALERT.OK, null, null);
            LoadingDialog.getInstance(this).hide();
        }
    }
  


    void GetAllData() throws JSONException {
        JSONObject loginObject = new JSONObject();
        loginObject.put("event", Constants.EVENT_GET_DATA);
        loginObject.put("username", DataManager.getInstance().username);
        String mess = loginObject.toString();
        WebSocketClient.getInstance().requestToServer(mess, this);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Thêm mã xử lý nếu cần thiết
    }


}