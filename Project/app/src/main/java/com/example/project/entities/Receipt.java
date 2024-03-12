package com.example.project.entities;

import com.example.project.DataManager;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Receipt {
    public String id;
    public String id_book;
    public String status;
    public String first_name;
    public String last_name;
    public String gender;
    public String email;
    public String phone;
    public Timestamp date_start;
    public Timestamp date_return;

    public Book book;
    public Book getBookByID(){
        if(book != null) return book;
        Optional<Book> result = Arrays.stream(DataManager.getInstance().getBooks())
                .filter(book -> book.id.equals(id_book))
                .findFirst();
        book = result.orElse(null);
        return book;
    }
}
