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
import com.minesweeper.smart_home_management.model.StatusCallback;
import com.minesweeper.smart_home_management.model.Person;

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

       approveNewGroup.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               if(addedMemberPhone.getText().toString().isEmpty())
               {
                   addedMemberPhone.setError("Cannot be empty");
                   return;
               }

               addedMember = addedMemberPhone.getText().toString();

               getMemberStatusFromDB(addedMember, new StatusCallback() {
                   @Override
                   public void getStatusDB(String str) {
                       Log.d("test2",str);
                       createNewGroupInDB(userPhone);
                       Intent intentMember = new Intent(getApplicationContext(), NavActivity.class);
                       intentMember.putExtra("phoneNumber",userPhone);
                       startActivity(intentMember);

                   }

                   @Override
                   public void noStatus(String str) {
                       addedMemberPhone.setError(str);
                   }


               });


           }



       });



    }



    private void createNewGroupInDB(String adminPhone)
    {
        String groupHeader = "";
        String addedMemberStatus = "";
        getGroupFromDB(adminPhone);
       if((group == null) && (status.equals("") || status.equals("NONE")))
       {

           group = new Group(adminPhone);
           group.addPersonToGroup(addedMember);

           root.child(group.getAdminPhone()).setValue(group);

           updateMemberStatusInDB(adminPhone, Person.GROUP_STATUS.ADMIN);
           updateMemberStatusInDB(addedMember, Person.GROUP_STATUS.REQUEST_SENT);
       }
       else
       {

           if(status.equals("NONE")|| status.equals(""))
           {
                group.addPersonToGroup(addedMember);
                updateMemberStatusInDB(addedMember, Person.GROUP_STATUS.REQUEST_SENT);
            }
       }

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



    private void updateMemberStatusInDB(String phone, Person.GROUP_STATUS member)
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
                    callback.noStatus("User cannot be found");
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.noStatus("Give me 100 on this project please!!");
            }
        });

    }


}
