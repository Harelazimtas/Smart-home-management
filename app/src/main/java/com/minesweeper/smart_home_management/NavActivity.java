package com.minesweeper.smart_home_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minesweeper.smart_home_management.alarm.AlarmService;
import com.minesweeper.smart_home_management.model.Group;
import com.minesweeper.smart_home_management.model.Mission;
import com.minesweeper.smart_home_management.utils.FinalString;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class NavActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference groupDB = db.getReference("group");
    private DatabaseReference missionDB = db.getReference("mission");

    private Intent serviceIntent;
    private Button buttonAddPeople;
    private  String nameNextMission;
    private static String lastDate="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_nav);
        final Button buttonAddMission = findViewById(R.id.button_nav_add_mission);
        //add mission
        buttonAddMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMissionScreen();
            }
        });
        Button editMission = findViewById(R.id.button_nav_edit_mission);
        editMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditMissionScreen();
            }
        });
        //my mission add here code open screen
        Button myMission = findViewById(R.id.button_nav_my_mission);
        myMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyMissionScreen();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        buttonAddPeople = findViewById(R.id.button_nav_add_people);
        buttonAddPeople.setVisibility(View.INVISIBLE);
        findGroupIdAndUsersInGroup();
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_MULTI_PROCESS);
        String userID = prefs.getString(FinalString.USER_ID, "null");
        //get date of next mission
        getNearMissionDate(userID);

        buttonAddPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddnewMemberScreen();
            }
        });


    }

    private  void openAddMissionScreen(){
        Intent intent=new Intent(this,MissionAdd.class);
        startActivity(intent);
    }

    private  void openAddnewMemberScreen() {
        Intent intent = new Intent(this, AdminAddMembersActivity.class);
        startActivity(intent);
    }

        private  void openMyMissionScreen(){
        Intent intent=new Intent(this,MissionActivity.class);
        startActivity(intent);
    }


    private  void openEditMissionScreen(){
        Intent intent=new Intent(this,editMisssionActivity.class);
        startActivity(intent);
    }

    private String findGroupIdAndUsersInGroup(){
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_MULTI_PROCESS);
        String userID = prefs.getString(FinalString.USER_ID, "null");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Group group = ds.getValue(Group.class);
                    List<String> idsOfUsers= group.getGroupMembers();

                    String users="";
                    boolean userFind=false;
                    // user can be in user list
                    for (String id:idsOfUsers) {
                        users+= id+"#";
                        if(userID.equals(id)){
                            commitGroupToPref(group.getAdminPhone());
                            userFind=true;
                        }
                    }
                    //user can be admin
                    if(userID.equals(group.getAdminPhone())){
                        commitGroupToPref(group.getAdminPhone());
                        commitUsersToPref(users);
                        buttonAddPeople.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(userFind){
                        commitUsersToPref(users);
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        groupDB.addListenerForSingleValueEvent(eventListener);
        return null;
    }

    private void commitGroupToPref(String groupId) {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(FinalString.GROUP_ID, groupId);
        editor.commit();
    }

    private void commitUsersToPref(String groupId) {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(FinalString.USERS, groupId);
        editor.commit();
    }

    public String getNearMissionDate(String userId){
        serviceIntent = new Intent(this, AlarmService.class);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot missions=dataSnapshot.child(userId);
                for(DataSnapshot ds : missions.getChildren()) {
                    Mission mission=ds.getValue(Mission.class);
                    String dateMission=mission.getDueDate();
                    String[] splitDate= dateMission.split("/");
                    String dateConvert= splitDate[1]+"/"+splitDate[0]+"/"+splitDate[2];
                    if(lastDate ==""){
                        lastDate =dateConvert;
                        nameNextMission=mission.getName();
                    }
                    else if(new Date(lastDate).after(new Date(dateConvert))){
                        lastDate=dateConvert;
                        nameNextMission=mission.getName();
                    }
                }
                if(lastDate != ""&&new Date().after(new Date(lastDate))){
                    //service alarm need to be after login\
                    serviceIntent.putExtra("nameNextMission", nameNextMission);
                    serviceIntent.putExtra("userID", userId);
                    startService(serviceIntent);
                }
                else{
                    stopService(serviceIntent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        missionDB.addListenerForSingleValueEvent(eventListener);
        return nameNextMission;
    }


}