package com.minesweeper.smart_home_management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minesweeper.smart_home_management.model.Mission;
import com.minesweeper.smart_home_management.utils.FinalString;
import com.minesweeper.smart_home_management.utils.MissionAdapter;
import com.minesweeper.smart_home_management.utils.RequestsAdapter;

import java.util.ArrayList;

public class MissionActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    //DB connection
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference missionDB = db.getReference("mission");
    private ArrayList<Mission> myMission=new ArrayList<>();
    private MissionAdapter adapter=new MissionAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.mission_activity);
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_MULTI_PROCESS);
        String userId= prefs.getString(FinalString.USER_ID, "null");


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot missions= dataSnapshot.child(userId);
                myMission.clear();
                for(DataSnapshot oneMission : missions.getChildren()) {
                    Mission missionOfUser=oneMission.getValue(Mission.class);
                    myMission.add(missionOfUser);

                }
                // notify list of mission to update
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        missionDB.addListenerForSingleValueEvent(eventListener);

        setAdapterRecyclerView();

    }

    private  void setAdapterRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewMission);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setMissions(myMission);
        recyclerView.setAdapter(adapter);
        adapter.setClickListener(new MissionAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openEditMissionScreen();
            }

        });
    }

    private  void openEditMissionScreen(){
        finish();
        Intent intent=new Intent(this,editMisssionActivity.class);
        startActivity(intent);
    }

}