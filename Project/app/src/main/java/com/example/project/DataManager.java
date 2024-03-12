package com.example.project;

import com.example.project.entities.Book;
import com.example.project.entities.DataResponse;
import com.example.project.entities.Receipt;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class DataManager {
    private static DataManager instance;

    private Book[] books;
    private Receipt[] receipts;

    private List<Book> booksSelect = new ArrayList<>();

    private DataManager() {

    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void UpdateData(DataResponse dataResponse){
        this.books = dataResponse.getBooks();
        this.receipts = dataResponse.getReceipts();
    }

    public Book[] getBooks() {
        return books;
    }

    public Receipt[] getReceipts() {
        return receipts;
    }

    public List<Book> getBooksSelect() {
        return booksSelect;
    }

}
