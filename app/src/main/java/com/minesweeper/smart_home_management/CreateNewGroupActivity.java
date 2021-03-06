package com.minesweeper.smart_home_management;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.minesweeper.smart_home_management.model.Group;
import com.minesweeper.smart_home_management.model.GroupCallback;
import com.minesweeper.smart_home_management.model.StatusCallback;
import com.minesweeper.smart_home_management.model.Person;
import com.minesweeper.smart_home_management.utils.FinalString;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class CreateNewGroupActivity extends AppCompatActivity {
    Group group;
    String userPhone = "";
    EditText addedMemberPhone;
    Button approveNewGroup;
     String status = "";
    String addedMember = "";
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference("group");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createnewgroup);

        Intent intent = getIntent();
        userPhone = intent.getStringExtra("phoneNumber");
        addedMemberPhone = findViewById(R.id.member_phone_txt);
       approveNewGroup = (Button) findViewById(R.id.create_group_btn);
        addedMemberPhone.setOnClickListener(this::onClick);

      approveNewGroup.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              if(!addedMemberPhone.getText().toString().equals(""))

              {
                  getMemberStatusFromDB(addedMemberPhone.getText().toString(), new StatusCallback() {
                      @Override
                      public void getStatusDB(String str) {
                         if(status.equals("NONE") || status.equals("") || status.equals("REQUEST_SENT"))
                                isUserValid();
                         else
                             {
                                 addedMemberPhone.setError("This user is in another group");
                                 addedMemberPhone.requestFocus();
                             }
                      }

                      @Override
                      public void noStatus(String str) {
                          addedMemberPhone.setError(str);
                          addedMemberPhone.requestFocus();
                      }
                  });



              }
              else
              {
                  addedMemberPhone.setError("Added user cannot be empty");
                  addedMemberPhone.requestFocus();
              }

          }
      });


    }



    private void createNewGroupInDB(String adminPhone)
    {
        String groupHeader = "";
        String addedMemberStatus = addedMemberPhone.getText().toString();

       if((group == null) && (status.equals("") || status.equals("NONE") || status.equals("REQUEST_SENT")))
       {
           group = new Group(adminPhone);
           group.addPersonToGroup(addedMemberStatus);
           root.child(group.getAdminPhone()).setValue(group);

           SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_MULTI_PROCESS);
           SharedPreferences.Editor editor = prefs.edit();
           editor.putString(FinalString.USER_ID,adminPhone);
           editor.commit();

           updateMemberStatusInDB(adminPhone, Person.GROUP_STATUS.ADMIN);
           updateMemberStatusInDB(addedMemberStatus, Person.GROUP_STATUS.REQUEST_SENT);
           Intent intent = new Intent(getApplicationContext(), NavActivity.class);
           intent.putExtra("phoneNumber",userPhone);
           startActivity(intent);
           finish();

       }
       else
       {

           if(status.equals("NONE")|| status.equals(""))
           {
                group.addPersonToGroup(addedMemberStatus);
                updateMemberStatusInDB(addedMemberStatus, Person.GROUP_STATUS.REQUEST_SENT);

            }
       }

    }


    public static void updateMemberStatusInDB(String phone, Person.GROUP_STATUS member)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("subscribers").child(phone).child("status");
        reference.setValue(member);

    }

    public void getMemberStatusFromDB(String phone, StatusCallback callback)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("subscribers").child(phone);
        Query ref = reference.orderByChild(phone);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot != null){
                    for (DataSnapshot snap: snapshot.getChildren()) {

                        if (snap.getKey().equals("status")) {
                                status = snap.getValue(String.class);
                                 callback.getStatusDB(status);
                                return;
                        }
                    }

                }
                else
                {
                    callback.noStatus("User cannot be found");
                    return;
            }   }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.noStatus("NO STATUS WAS FOUND");
            }
        });

    }
    public void onClick(View v) {
        addedMemberPhone.getText().clear();
    }


    private void isUserValid()
    {
        String addedMember = addedMemberPhone.getText().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("subscribers");
        reference.child(addedMember).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if(addedMember.equals(userPhone))
                    {
                        addedMemberPhone.setError("You cannot create a group with yourself!");
                        addedMemberPhone.requestFocus();
                        return;
                    }

                    createNewGroupInDB(userPhone);
                    return;

                }
                addedMemberPhone.setError("Added user was not found");
                addedMemberPhone.requestFocus();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}
