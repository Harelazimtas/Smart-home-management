package com.minesweeper.smart_home_management;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.minesweeper.smart_home_management.model.Mission;
import com.minesweeper.smart_home_management.model.Status;
import com.minesweeper.smart_home_management.utils.FinalString;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class editMisssionActivity extends AppCompatActivity {
    private Spinner spinner,spinnertStatus;
    //DB connection
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference missionDB = db.getReference("mission");

    //field editText-date
    private EditText editDueDate;
    private int day,month,year;
    private final Calendar mcalendar=Calendar.getInstance();

    //data for dropdown
    private List<Mission> missionUser=new ArrayList();
    private List<String> missionName=new ArrayList();
    private List<Status> statusList=new ArrayList();
    private Mission mission=new Mission();
    //
    private String currentNameMission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_misssion);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_MULTI_PROCESS);
        String userId= prefs.getString(FinalString.USER_ID, "null");
        statusList.add(Status.NEW);
        statusList.add(Status.IN_PROGRESS);
        statusList.add(Status.FINISH);

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,missionName);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot missions= dataSnapshot.child(userId);
                missionUser.clear();
                missionName.clear();
                for(DataSnapshot oneMission : missions.getChildren()) {
                    Mission missionOfUser=oneMission.getValue(Mission.class);
                    missionUser.add(missionOfUser);
                    missionName.add(missionOfUser.getName());
                }
                //refresh the drop down
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        missionDB.addListenerForSingleValueEvent(eventListener);

        //text field
        EditText editName = (EditText) findViewById(R.id.field_name_mission_edit);
        EditText editDescription = (EditText) findViewById(R.id.field_description_mission);
        editDueDate = (EditText) findViewById(R.id.field_due_date_mission);

        //date init
        editDueDate.setOnClickListener(mClickListener);
        day=mcalendar.get(Calendar.DAY_OF_MONTH);
        year=mcalendar.get(Calendar.YEAR);
        month=mcalendar.get(Calendar.MONTH);

        // spinner mission
        spinnertStatus=(Spinner)findViewById(R.id.field_status_mission);
        ArrayAdapter<Status> adapterStatus =new ArrayAdapter<Status>(this,
                android.R.layout.simple_list_item_1,statusList);

        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertStatus.setAdapter(adapterStatus);

        // spinner mission
        spinnertStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mission.setStatus(statusList.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });



        // button submit validate field
        final Button button = findViewById(R.id.button_field_submit);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mission.setIdPerson(userId);

                String name=String.valueOf(editName.getText());
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
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Invalid Date", Toast.LENGTH_LONG).show();

                }
                //remove mission if the name change or if the mission finish
                if(!currentNameMission.equals(mission.getName())){
                    missionDB.child(mission.getIdPerson()+"").child(currentNameMission).removeValue();
                }
                if(mission.getStatus().equals(Status.FINISH)){
                    missionDB.child(mission.getIdPerson()+"").child(currentNameMission).removeValue();
                    return;
                }
                // update mission to db IdPerson/name_mission/mission
                Toast.makeText(getApplicationContext(),"Mission edit success", Toast.LENGTH_LONG).show();

                missionDB.child(mission.getIdPerson()+"").child(mission.getName()).setValue(mission);
            }
        });


        //spinner-dropdown name mission
        spinner = (Spinner) findViewById(R.id.field_assign_mission);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Mission missionSlected= missionUser.get(position);
                currentNameMission=missionSlected.getName();
                editName.setText(missionSlected.getName());
                editDescription.setText(missionSlected.getDescription());
                editDueDate.setText(missionSlected.getDueDate());
                int index=0;
                if(missionSlected.getStatus() == Status.NEW){
                    index=0;
                }
                else if(missionSlected.getStatus() == Status.IN_PROGRESS){
                    index=1;
                }
                else if(missionSlected.getStatus() ==Status.FINISH) {
                    index=2;
                }
                spinnertStatus.setSelection(index);

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });


    }

    //  date code
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