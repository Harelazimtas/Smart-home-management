package com.minesweeper.smart_home_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minesweeper.smart_home_management.model.Group;
import com.minesweeper.smart_home_management.model.Person;

public class CreateNewGroupActivity extends AppCompatActivity {
    Group group;
    String userPhone = "";
    EditText addedMemberPhone;
    Button approveNewGroup;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference("group");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnewgroup);

        Intent intent = getIntent();
        userPhone = intent.getStringExtra("phoneNumber");
        addedMemberPhone = findViewById(R.id.member_phone_txt);
        approveNewGroup = (Button) findViewById(R.id.create_group_btn);
        approveNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewGroupInDB(userPhone);
            }
        });



    }



    private void createNewGroupInDB(String adminPhone)
    {
        String groupHeader = "";
        String addedMember = addedMemberPhone.getText().toString();
        group = new Group(adminPhone);
        group.addPersonToGroup(addedMember);

        root.child(group.getAdminPhone()).setValue(group);

        updateMemberStatus(adminPhone, Person.GROUP_STATUS.ADMIN);
    }




    private void getGroupFromDB(String phoneFromUser)
    {


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("group");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isMemberFound = false;
                for (DataSnapshot snap:snapshot.getChildren()){

                    group = new Group(snap.getKey());
                    for(DataSnapshot innerSnap :snapshot.child("phoneNumber").getChildren())
                    {
                        String phoneNumber = innerSnap.getValue(String.class);
                        group.addPersonToGroup(phoneNumber);
                        if(innerSnap.getValue(String.class) == phoneFromUser)
                        {
                            isMemberFound = true;

                        }

                    }

                    if(isMemberFound)
                    {
                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    private void updateMemberStatus(String phone, Person.GROUP_STATUS member)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("subscribers").child(phone).child("status");
        reference.setValue(member);

    }

}
