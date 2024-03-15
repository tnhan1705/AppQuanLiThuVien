package com.example.project.entities;

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
}
