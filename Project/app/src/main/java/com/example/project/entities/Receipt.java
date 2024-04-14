package com.example.project.entities;

import com.example.project.DataManager;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Receipt implements Serializable {
    public String id;
    public String id_books;

    public String name;
    public String summary;
    public String name_author;
    public String category;
    public int inventory_quantity;
    public String image;
    public String status;
    public String first_name;
    public String last_name;
    public String gender;
    public String email;
    public String phone;
    public Timestamp date_start;
    public Timestamp date_return;
    public String date_add;

    public Book[] books;

    public Book getBookByID(String id){
        Optional<Book> result = Arrays.stream(DataManager.getInstance().getBooks())
                .filter(book -> book.id.equals(id))
                .findFirst();
        return result.orElse(null);
    }

    public Book[] getBooksByIDs(){
        if(books != null) return books;
        String[] IDs = id_books.split(",");
        books = new Book[IDs.length];
        for (int i = 0; i < IDs.length; i++) {
            books[i] = getBookByID(IDs[i]);
        }
        return books;
    }
}
