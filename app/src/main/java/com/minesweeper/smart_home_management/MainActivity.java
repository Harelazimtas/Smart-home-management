package com.minesweeper.smart_home_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start_button = findViewById(R.id.button1);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMissioncreen();
            }


        });
    }

    private  void openAddMissioncreen(){
        Intent intent=new Intent(this,MissionAdd.class);
        startActivity(intent);
    }



}