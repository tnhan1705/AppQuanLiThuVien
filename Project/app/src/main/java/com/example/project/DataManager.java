package com.example.project;

import com.example.project.entities.Book;
import com.example.project.entities.DataResponse;
import com.example.project.entities.Receipt;
import com.example.project.entities.User;
import com.google.gson.Gson;


import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.var;

@AllArgsConstructor
public class DataManager {
    private static DataManager instance;

    public String username;
    private Book[] books;
    public Book[] booksFilter;
    public Receipt[] receipts;

    public List<Book> booksSelect = new ArrayList<>();
    public User user;

    private DataManager() {

    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public Book getBookByID(String id){
        for (Book book : books) {
            if(book.id.equals(id)){
                return book;
            }
        }
        return null;
    }

    public void UpdateData(DataResponse dataResponse){
        this.books = dataResponse.getBooks();
        this.receipts = dataResponse.getReceipts();
    }

    public Book[] getBooks() {
        if(booksFilter != null) return booksFilter;
        return books;
    }

    public void addBookSelect(Book book){
        booksSelect.add(book);
    }

    public Receipt[] getReceipts() {
        return receipts;
    }

    public List<Book> getBooksSelect() {
        return booksSelect;
    }

    public void getUser(String data) {
        // Khởi tạo một đối tượng Gson
        Gson gson = new Gson();

        // Chuyển đổi chuỗi JSON thành một đối tượng User
        this.user = gson.fromJson(data, User.class);
    }

    public User getUser() {
        return this.user;
    }
}
