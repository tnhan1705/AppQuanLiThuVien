package com.example.project.ui;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.DataManager;
import com.example.project.R;
import com.example.project.entities.Book;
import com.example.project.ui.custom_adapter.CustomBookAdapter;
import com.example.project.ui.custom_adapter.CustomDetailOrderAdapter;
import com.example.project.utils.UIService;

public class DetailOrderActivity extends AppCompatActivity {

    private ListView listView;
    private CustomDetailOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIService.HideStatusBar(this, this);
        setContentView(R.layout.activity_detail_order);

        listView = findViewById(R.id.listView);
        adapter = new CustomDetailOrderAdapter(this, R.layout.list_item_book_review);

        // Assuming DataManager.getInstance().getBooks() returns an array of Book objects
        Book[] books = DataManager.getInstance().getBooksSelect().toArray(new Book[0]);
        adapter.addAll(books); // Pass the array of Book objects to the adapter

        listView.setAdapter(adapter);
    }


}
