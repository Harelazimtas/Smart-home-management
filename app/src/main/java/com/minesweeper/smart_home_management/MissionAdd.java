package com.minesweeper.smart_home_management;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minesweeper.smart_home_management.alarm.AlarmService;
import com.minesweeper.smart_home_management.model.Mission;
import com.minesweeper.smart_home_management.model.Status;
import com.minesweeper.smart_home_management.utils.FinalString;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MissionAdd extends AppCompatActivity{
    private Spinner spinner;
    private Mission mission;
    //DB connection
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference userDB = db.getReference("subscribers");
    private DatabaseReference missionDB = db.getReference("mission");

    //field editText-date
    private EditText editDueDate;
    private int day,month,year;
    private final Calendar mcalendar=Calendar.getInstance();

    //data for dropdown
    private   List<String> userName=new ArrayList();
    private  List<String> ids=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_add);
        mission=new Mission();

        //add user name to list by group id and init id of user for dropdown
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_MULTI_PROCESS);
        String users= prefs.getString(FinalString.USERS, "null");
        String[] usersId= users.split("#");

        String groupId= prefs.getString(FinalString.GROUP_ID, "null");

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,userName);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue(String.class);
                    String id = ds.child("phoneNumber").getValue(String.class);
                    String status= ds.child("status").getValue(String.class);
                    // user in the group
                    if(!status.equals("NONE") && ArrayUtils.contains( usersId, id )){
                        userName.add(name);
                        ids.add(id);
                    }
                    //admin
                    if(id.equals(groupId) ){
                        userName.add(name);
                        ids.add(id);
                    }
                }
                //refresh the drop down
               adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        userDB.addListenerForSingleValueEvent(eventListener);

        //text field
        EditText editName = (EditText) findViewById(R.id.field_name_mission_edit);
        EditText editDescription = (EditText) findViewById(R.id.field_description_mission);
        editDueDate = (EditText) findViewById(R.id.field_due_date_mission);

        //date init
        editDueDate.setOnClickListener(mClickListener);
        day=mcalendar.get(Calendar.DAY_OF_MONTH);
        year=mcalendar.get(Calendar.YEAR);
        month=mcalendar.get(Calendar.MONTH);


        // button submit
        final Button button = findViewById(R.id.button_field_submit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name=String.valueOf(editName.getText());
                // check input
                if (name.length() !=0) {
                    mission.setName(name);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Invalid name", Toast.LENGTH_LONG).show();
                    return;
                }
                String description=String.valueOf(editDescription.getText());
                if(description.length() !=0){
                    mission.setDescription(description);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Invalid description", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    mission.setDueDate(String.valueOf(editDueDate.getText()));
                    mission.setStatus(Status.NEW);
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Invalid Date", Toast.LENGTH_LONG).show();

                }
                // add mission to db IdPerson/name_mission/mission
                missionDB.child(mission.getIdPerson()+"").child(mission.getName()).setValue(mission);
            }
        });


        //spinner-dropdown
        spinner = (Spinner) findViewById(R.id.field_assign_mission);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("selected: ", String.valueOf(position));
                mission.setIdPerson(Integer.parseInt(ids.get(position)));
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

    }


    View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DateDialog();
        }
    };
    public void DateDialog(){
        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                editDueDate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
            }};
        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, year, month, day);
        dpDialog.show();
    }




}
