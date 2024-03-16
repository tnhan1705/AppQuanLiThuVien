package com.example.project;

import com.example.project.entities.Book;
import com.example.project.entities.DataResponse;
import com.example.project.entities.Receipt;

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


}
