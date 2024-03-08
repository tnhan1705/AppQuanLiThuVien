package com.example.project.entities;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Receipt {
    private String id;
    private String id_book;
    private String status;
    private String first_name;
    private String last_name;
    private String gender;
    private String email;
    private String phone;
    private Timestamp date_start;
    private Timestamp date_return;
}
