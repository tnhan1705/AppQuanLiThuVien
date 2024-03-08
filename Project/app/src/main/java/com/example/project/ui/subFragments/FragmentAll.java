package com.example.project.ui.subFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.project.DataManager;
import com.example.project.R;
import com.example.project.entities.Book;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAll#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAll extends Fragment {

    private ListView listView;
    private CustomBookAdapter adapter;

    public FragmentAll() {
        // Required empty public constructor
    }

    public static FragmentAll newInstance(String param1, String param2) {
        FragmentAll fragment = new FragmentAll();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        listView = view.findViewById(R.id.listView);
        adapter = new CustomBookAdapter(requireContext(), R.layout.list_item_book);

        // Assuming DataManager.getInstance().getBooks() returns an array of Book objects
        Book[] books = DataManager.getInstance().getBooks();
        adapter.addAll(books); // Pass the array of Book objects to the adapter

        listView.setAdapter(adapter);

        return view;
    }
}