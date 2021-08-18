package com.minesweeper.smart_home_management;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minesweeper.smart_home_management.model.Group;
import com.minesweeper.smart_home_management.model.Person;
import com.minesweeper.smart_home_management.utils.FinalString;

public class AdminAddMembersActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference rootGroup = db.getReference("group");
    private DatabaseReference rootSub = db.getReference("subscribers");
    private EditText newMemberInGroup;
    private Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminaddusertogroup);
        newMemberInGroup = findViewById(R.id.new_mem_edit_txt);
        addButton = findViewById(R.id.add_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkMemberStatus();
            }
        });


    }



    private void checkMemberStatus() {
        rootSub.child(newMemberInGroup.getText().toString()).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String status = snapshot.getValue(String.class);
                    if (status.equals("") || status.equals("REQUEST_SENT") || status.equals("NONE")) {
                        addMemberToGroup();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "This member is in another group", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addMemberToGroup()
    {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_MULTI_PROCESS);
        String adminPhone = prefs.getString(FinalString.USER_ID, "null");
        Group group = new Group(adminPhone);

        group.addPersonToGroup(newMemberInGroup.getText().toString());
        CreateNewGroupActivity.updateMemberStatusInDB(newMemberInGroup.getText().toString(), Person.GROUP_STATUS.REQUEST_SENT);
        rootGroup.child(adminPhone).child("groupMembers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {

                if(snapshot.exists())
                {
                    boolean isUser = false;
                    for (DataSnapshot snap: snapshot.getChildren())
                    {
                        String phone = snap.getValue(String.class);
                        if(!phone.equals(newMemberInGroup.getText().toString()))
                            group.addPersonToGroup(phone);
                        else
                        {
                            isUser = true;
                        }

                    }
                    if(!isUser) {
                        rootGroup.child(adminPhone).child("groupMembers").setValue(group.getGroupMembers()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getApplicationContext(), "Member was added successfully", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Member is already in this group", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}
