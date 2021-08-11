package com.minesweeper.smart_home_management;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minesweeper.smart_home_management.model.Group;
import com.minesweeper.smart_home_management.model.StatusCallback;

public class RequestApproveActivity extends AppCompatActivity {
    String LoggedInUserFromDB = "";
    String loggedInUser = "";
    Group group;
    String status = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestgroup);

        Intent intent = getIntent();
        LoggedInUserFromDB = intent.getStringExtra("name");
       // loggedInUser = findViewById(R.id.nameFromDB);
        getGroupFromDB(LoggedInUserFromDB, new StatusCallback() {
            @Override
            public void getStatusDB(String str) {
                Log.d("test2",str);
            }
            @Override
            public void noStatus(String str) {
                Log.d("test3", str);
            }
        });


    }


    private void getGroupFromDB(String phoneFromUser, StatusCallback callback)
    {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("group");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isMemberFound = false;
                for (DataSnapshot snap:snapshot.getChildren()){

                    group = new Group(snap.getKey());
                    for(DataSnapshot innerSnap :snap.child("phoneNumber").getChildren())
                    {
                        String phoneNumber = innerSnap.getValue(String.class);
                        group.addPersonToGroup(phoneNumber);
                        if(innerSnap.getValue(String.class) == phoneFromUser)
                        {
                            isMemberFound = true;
                            callback.getStatusDB(phoneFromUser);

                        }

                    }
/*
                    if(isMemberFound)
                    {
                        return;
                    }
*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
