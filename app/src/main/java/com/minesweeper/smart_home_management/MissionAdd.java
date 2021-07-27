package com.minesweeper.smart_home_management;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.minesweeper.smart_home_management.model.Mission;

import java.util.Date;

public class MissionAdd extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private Mission mission;
    //data for dropdown
    private static final String[] paths = {"item 1", "item 2", "item 3"};
    private static final int[] ids={1,2,3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_add2);

        mission=new Mission();

        //spinner-dropdown
        spinner = (Spinner)findViewById(R.id.field_assign_mission);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MissionAdd.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //text field
        EditText editName = (EditText) findViewById(R.id.field_name_mission_edit);
        EditText editDescription = (EditText) findViewById(R.id.field_description_mission);
        EditText editDueDate = (EditText) findViewById(R.id.field_due_date_mission);




        // button submit
        final Button button = findViewById(R.id.button_field_submit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mission.setName(String.valueOf(editName.getText()));
                mission.setDescription(String.valueOf(editDescription.getText()));
                mission.setDueDate(new Date(String.valueOf(editDueDate.getText())));
                System.out.println("mission111 "+mission);
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mission.setId(ids[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}
