package com.example.project.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Book {
    public String id;
    public String name;
    public String summary;
    public String name_author;
    public int inventory_quantity;
}
