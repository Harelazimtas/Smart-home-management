package com.minesweeper.smart_home_management;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity  extends AppCompatActivity {
    private String LoggedInUserFromDB;
    private TextView loggedInUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        LoggedInUserFromDB = intent.getStringExtra("name");
        loggedInUser = findViewById(R.id.nameFromDB);
    }
}