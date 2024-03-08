package com.example.project.ui.subFragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.project.R;
import com.example.project.entities.Book;

// CustomBookAdapter.java
public class CustomBookAdapter extends ArrayAdapter<Book> {

    public CustomBookAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_book, parent, false);
        }

        // Get the current Book object
        Book book = getItem(position);

        // Set the data to the views in the list item layout
        TextView itemTextView = convertView.findViewById(R.id.itemTextView);
        TextView itemTextView1 = convertView.findViewById(R.id.itemTextView1);
        TextView itemTextView2 = convertView.findViewById(R.id.itemTextView2);

        if (book != null) {
            itemTextView.setText(book.name);
            itemTextView1.setText(book.name_author);
            itemTextView2.setText(book.summary);
        }

        return convertView;
    }
}
