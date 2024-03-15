package com.example.project.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.entities.Receipt;
import com.example.project.utils.UIService;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

public class DetailReceiptActivity extends AppCompatActivity {

    EditText editDateFrom;
    EditText editDateTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIService.HideStatusBar(this, this);
        setContentView(R.layout.activity_detail_receipt);

        ImageButton btnback = findViewById(R.id.btnBack);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Nhận Intent từ hoạt động trước
        Intent intent = getIntent();

        // Kiểm tra xem Intent có dữ liệu không
        if (intent.hasExtra("receipt")) {
            Receipt receipt = (Receipt) intent.getSerializableExtra("receipt");
            View includedLayout = findViewById(R.id.container); // R.id.container is the ID of the parent layout in DetailReceiptActivity

            TextView txtName = includedLayout.findViewById(R.id.textName);
            TextView txtAuthorName = includedLayout.findViewById(R.id.textAuthorName);
            TextView txtBorrowerName = includedLayout.findViewById(R.id.textBorrowerName);
            TextView txtTimeStart = includedLayout.findViewById(R.id.textTimeStart);
            TextView txtStatus = includedLayout.findViewById(R.id.textStatus);

            Timestamp now =  new Timestamp(System.currentTimeMillis());
            txtStatus.setTextColor(receipt.date_return.after(now) ? Color.parseColor("#1DD75B") : Color.parseColor("#DE3B40"));
            LocalDateTime dateReturnLocalDateTime = receipt.date_return.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime nowLocalDateTime = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            Duration duration = Duration.between(dateReturnLocalDateTime, nowLocalDateTime);
            long days = Math.abs(duration.toDays());
            long hours = Math.abs(duration.toHours() % 24);
            txtStatus.setText((receipt.date_return.after(now) ? "Remain: " : "Expired: ") + (days > 0 ? days + " days" : hours + " hours"));

            txtName.setText(receipt.getBookByID().name);
            txtAuthorName.setText(receipt.getBookByID().name_author);
            txtBorrowerName.setText(receipt.first_name + receipt.last_name);
            txtTimeStart.setText("Start: " + receipt.date_start.toString());
        }

        editDateFrom = findViewById(R.id.editDateFrom);
        editDateFrom.setEnabled(false);
        editDateFrom.setBackgroundResource(R.drawable.ip_disable);

        editDateTo = findViewById((R.id.editDateTo));

        ImageView btnCalendar = findViewById(R.id.btnDateTo);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
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
    public void onBackPressed() {
        super.onBackPressed();
        // Thêm mã xử lý nếu cần thiết
    }
}