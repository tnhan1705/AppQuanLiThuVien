package com.example.project.ui.custom_adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.project.DataManager;
import com.example.project.R;
import com.example.project.entities.Book;
import com.example.project.ui.subFragments.OnSelectButtonClickListener;

import java.util.List;

// CustomBookAdapter.java
public class CustomBookAdapter extends ArrayAdapter<Book> {

    private OnSelectButtonClickListener onSelectButtonClickListener;
    private SparseBooleanArray selectedItems;

    boolean isModeSelect;

    public void setOnSelectButtonClickListener(OnSelectButtonClickListener listener) {
        this.onSelectButtonClickListener = listener;
    }

    public CustomBookAdapter(Context context, int resource, boolean isModeSelect) {
        super(context, resource);
        selectedItems = new SparseBooleanArray();
        this.isModeSelect = isModeSelect;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_book, parent, false);
        }

        ImageButton button = convertView.findViewById(R.id.buttonAdd);

        View finalConvertView = convertView;

        // Get the current Book object
        Book book = getItem(position);
        List<Book> books = DataManager.getInstance().getBooksSelect();
        boolean containsBook = books.stream().anyMatch(b -> b.id.equals(book.id));
        if(containsBook && isModeSelect){
            // Đảo ngược trạng thái của mục
            selectedItems.put(position, !selectedItems.get(position));

            // Thay đổi màu nền của list_item_book
            int backgroundColor = selectedItems.get(position) ? ContextCompat.getColor(getContext(), R.color.itemSelected) : Color.WHITE;
            finalConvertView.setBackgroundColor(backgroundColor);
            button.setImageDrawable(selectedItems.get(position) ? ContextCompat.getDrawable(getContext(), R.drawable.tick_added) : ContextCompat.getDrawable(getContext(), R.drawable.button2));
        }
        else if(!isModeSelect){
            button.setVisibility(View.GONE);
        }

        // Set the data to the views in the list item layout
        TextView itemTextView = convertView.findViewById(R.id.textName);
        TextView itemTextView1 = convertView.findViewById(R.id.textAuthorName);
        TextView itemTextView2 = convertView.findViewById(R.id.textSummary);

        if (book != null) {
            itemTextView.setText(book.name);
            itemTextView1.setText(book.name_author);
            itemTextView2.setText(book.summary);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selectedItems.get(position)) DataManager.getInstance().getBooksSelect().add(book);
                else DataManager.getInstance().getBooksSelect().remove(book);
                onSelectButtonClickListener.onSelectButtonClick();

                // Đảo ngược trạng thái của mục
                selectedItems.put(position, !selectedItems.get(position));

                // Thay đổi màu nền của list_item_book
                int backgroundColor = selectedItems.get(position) ? ContextCompat.getColor(getContext(), R.color.itemSelected) : Color.WHITE;
                finalConvertView.setBackgroundColor(backgroundColor);
                button.setImageDrawable(selectedItems.get(position) ? ContextCompat.getDrawable(getContext(), R.drawable.tick_added) : ContextCompat.getDrawable(getContext(), R.drawable.button2));
            }
        });

        // Đặt màu nền tương ứng với trạng thái của mục
        int backgroundColor = selectedItems.get(position) ? ContextCompat.getColor(getContext(), R.color.itemSelected) : Color.WHITE;
        button.setImageDrawable(selectedItems.get(position) ? ContextCompat.getDrawable(getContext(), R.drawable.tick_added) : ContextCompat.getDrawable(getContext(), R.drawable.button2));
        convertView.setBackgroundColor(backgroundColor);

        return convertView;
    }
}