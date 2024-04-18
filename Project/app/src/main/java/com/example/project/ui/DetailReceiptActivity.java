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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.DataManager;
import com.example.project.R;
import com.example.project.entities.Book;
import com.example.project.entities.Receipt;
import com.example.project.utils.UIService;
import com.google.gson.Gson;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import android.view.LayoutInflater;


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

        LinearLayout container = findViewById(R.id.container_items_book);
        LayoutInflater inflater = LayoutInflater.from(this); // Tạo LayoutInflater

        // Kiểm tra xem Intent có dữ liệu không
        if (intent.hasExtra("receipt")) {
            Gson gson = new Gson();
            String json = intent.getStringExtra("receipt");
            Receipt receipt = gson.fromJson(json, Receipt.class);
            Book[] books = receipt.getBooksByIDs();

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

                // Thêm itemView vào LinearLayout
                container.addView(itemView);
            }
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