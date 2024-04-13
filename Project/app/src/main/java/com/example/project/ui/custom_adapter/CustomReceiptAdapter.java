package com.example.project.ui.custom_adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.project.DataManager;
import com.example.project.R;
import com.example.project.entities.Book;
import com.example.project.entities.Receipt;
import com.example.project.ui.DetailReceiptActivity;
import com.example.project.ui.subFragments.OnSelectButtonClickListener;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

// CustomBookAdapter.java
public class CustomReceiptAdapter extends ArrayAdapter<Receipt> {

    private OnSelectButtonClickListener onSelectButtonClickListener;

    public void setOnSelectButtonClickListener(OnSelectButtonClickListener listener) {
        this.onSelectButtonClickListener = listener;
    }

    public CustomReceiptAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_receipt, parent, false);
        }

        // Get the current Book object
        Receipt receipt = getItem(position);

        // Set the data to the views in the list item layout
        TextView txtName = convertView.findViewById(R.id.textName);
        TextView txtAuthorName = convertView.findViewById(R.id.textAuthorName);
        TextView txtBorrowerName = convertView.findViewById(R.id.textBorrowerName);
        TextView txtTimeStart = convertView.findViewById(R.id.textTimeStart);
        TextView txtStatus = convertView.findViewById(R.id.textStatus);

        Timestamp now =  new Timestamp(System.currentTimeMillis());
        txtStatus.setTextColor(receipt.date_return.after(now) ? Color.parseColor("#1DD75B") : Color.parseColor("#DE3B40"));
        LocalDateTime dateReturnLocalDateTime = receipt.date_return.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime nowLocalDateTime = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        Duration duration = Duration.between(dateReturnLocalDateTime, nowLocalDateTime);
        long days = Math.abs(duration.toDays());
        long hours = Math.abs(duration.toHours() % 24);
        txtStatus.setText((receipt.date_return.after(now) ? "Remain: " : "Expired: ") + (days > 0 ? days + " days" : hours + " hours"));

        txtName.setText(receipt.getBooksByIDs()[0].name);
        txtAuthorName.setText(receipt.getBooksByIDs()[0].name_author);
        txtBorrowerName.setText(receipt.first_name + receipt.last_name);
        txtTimeStart.setText("Start: " + receipt.date_start.toString());

        Button button = convertView.findViewById(R.id.btnSelect);

        View finalConvertView = convertView;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                // Start the new activity
                Intent intent = new Intent(context, DetailReceiptActivity.class);
                intent.putExtra("receipt", receipt);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}