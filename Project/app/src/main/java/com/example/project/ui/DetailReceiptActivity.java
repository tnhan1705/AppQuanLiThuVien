package com.example.project.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.DataManager;
import com.example.project.R;
import com.example.project.databinding.ActivityDetailReceiptBinding; // Import the generated binding class
import com.example.project.entities.Book;
import com.example.project.entities.Receipt;
import com.example.project.utils.UIService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

public class DetailReceiptActivity extends AppCompatActivity {

    private ActivityDetailReceiptBinding binding; // Declare a binding variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the binding
        binding = ActivityDetailReceiptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Set the content view to the root of the binding
        UIService.HideStatusBar(this, this);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (intent.hasExtra("receipt")) {
            Receipt receipt = (Receipt) intent.getSerializableExtra("receipt");
            Book[] books = receipt.getBooksByIDs();

            for (Book book : books) {
                View itemView = inflater.inflate(R.layout.list_item_book_review, null);
                TextView name_book = itemView.findViewById(R.id.name_book);
                TextView author_name = itemView.findViewById(R.id.author_name);
                TextView summary = itemView.findViewById(R.id.summary);

                name_book.setText(book.name);
                author_name.setText(book.name_author);
                summary.setText(book.summary);

                binding.containerItemsBook.addView(itemView);
            }
        }

        binding.editDateFrom.setEnabled(false);
        binding.editDateFrom.setBackgroundResource(R.drawable.ip_disable);

        binding.btnDateTo.setOnClickListener(new View.OnClickListener() {
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
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String formattedDate = String.format("%04d/%02d/%02d", year1, monthOfYear + 1, dayOfMonth);
                    binding.editDateTo.setText(formattedDate);
                }, year, month, day);

        datePickerDialog.show();
    }
}