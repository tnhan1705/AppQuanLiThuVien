package com.example.project.ui.subFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.project.DataManager;
import com.example.project.R;
import com.example.project.entities.Book;
import com.example.project.entities.Receipt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBorrowing#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBorrowing extends Fragment {
    private ListView listView;
    private CustomReceiptAdapter adapter;
    public FragmentBorrowing() {
        // Required empty public constructor
    }

    public static FragmentBorrowing newInstance(String param1, String param2) {
        FragmentBorrowing fragment = new FragmentBorrowing();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sub_fragment_borrowing, container, false);
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);

        listView = view.findViewById(R.id.listView);
        adapter = new CustomReceiptAdapter(requireContext(), R.layout.list_item_receipt);
        adapter.setOnSelectButtonClickListener(new OnSelectButtonClickListener(){

            @Override
            public void onSelectButtonClick() {

            }
        });

        // Assuming DataManager.getInstance().getBooks() returns an array of Book objects
        Receipt[] receipts = DataManager.getInstance().getReceipts();
        adapter.addAll(receipts); // Pass the array of Book objects to the adapter

        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return view;
    }
}