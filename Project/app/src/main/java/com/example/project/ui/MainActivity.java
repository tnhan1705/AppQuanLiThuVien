package com.example.project.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.utils.UIService;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIService.HideStatusBar(this, this);
        setContentView(R.layout.activity_main);


    }
}
