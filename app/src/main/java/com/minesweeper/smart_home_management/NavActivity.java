package com.minesweeper.smart_home_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);


        final Button buttonAddMission = findViewById(R.id.button_nav_add_mission);
        buttonAddMission.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openAddMissionScreen();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Button buttonAddPeople = findViewById(R.id.button_nav_add_people);
        buttonAddPeople.setVisibility(View.INVISIBLE);

    }

    private  void openAddMissionScreen(){
        Intent intent=new Intent(this,MissionAdd.class);
        startActivity(intent);
    }

    private  void openAddPeopleScreen(){
        Intent intent=new Intent(this,MissionAdd.class);
        startActivity(intent);
    }
}