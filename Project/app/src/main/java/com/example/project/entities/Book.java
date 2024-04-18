package com.example.project.entities;

import android.graphics.Bitmap;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Book implements Serializable {

    public String id;

    public String name;
    public String summary;
    public String name_author;
    public int inventory_quantity;
    public String image;
    public String category;

    public  String dateToAdd;

    public transient Bitmap decodedByte;

    public Book() {
    }

    public Book(String id, String name, String summary,
                String name_author, int inventory_quantity, String image, String category, String dateToAdd) {
        this.id = id;
        this.name = name;
        this.summary = summary;
        this.category = category;
        this.name_author = name_author;
        this.inventory_quantity = inventory_quantity;
        this.image = image;
        this.dateToAdd = dateToAdd;
    }

    public String getDateToAdd() {
        return dateToAdd;
    }

    public void setDateToAdd(String dateToAdd) {
        this.dateToAdd = dateToAdd;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setName_author(String name_author) {
        this.name_author = name_author;
    }

    public void setInventory_quantity(int inventory_quantity) {
        this.inventory_quantity = inventory_quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getName_author() {
        return name_author;
    }

    public int getInventory_quantity() {
        return inventory_quantity;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", name_author='" + name_author + '\'' +
                ", inventory_quantity=" + inventory_quantity +
                '}';
    }
}
