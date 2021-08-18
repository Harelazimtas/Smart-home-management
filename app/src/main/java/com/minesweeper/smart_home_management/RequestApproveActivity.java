package com.minesweeper.smart_home_management;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minesweeper.smart_home_management.model.Group;
import com.minesweeper.smart_home_management.model.GroupCallback;
import com.minesweeper.smart_home_management.model.Person;
import com.minesweeper.smart_home_management.model.RequestsCallback;
import com.minesweeper.smart_home_management.utils.RequestsAdapter;

import java.util.ArrayList;
import java.util.List;

public class RequestApproveActivity extends AppCompatActivity {
    String LoggedInUserFromDB = "";
    String loggedInUser = "";
    List<String> arrGroups;
    Group group;
    String status = "";
    private RecyclerView recyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestgroup);
        Intent intent = getIntent();
        LoggedInUserFromDB = intent.getStringExtra("phoneNumber");
        arrGroups = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        setAdapter();

        gettingAllRequests(LoggedInUserFromDB, new RequestsCallback() {
            @Override
            public void getRequestsString(List<String> list) {
                arrGroups = list;
                showAllRequests();

            }

            @Override
            public void noRequests(String str) {
                Log.d("kenny", str);

            }
        });


    }



    private void gettingAllRequests(String phoneFromUser, RequestsCallback callback)
    {
        List<String> arrAdminsPhone = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("group");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()) {
                    for(DataSnapshot innerSnap :snap.child("groupMembers").getChildren())
                    {
                        if(innerSnap.getValue().equals(phoneFromUser))
                        {
                            arrAdminsPhone.add(snap.getKey());
                            break;
                        }
                    }

                }
                if(arrAdminsPhone.size() > 0)
                    callback.getRequestsString(arrAdminsPhone);
                else
                    callback.noRequests("No groups were found");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void showAllRequests() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RequestsAdapter adapter_requests = new RequestsAdapter(arrGroups);
        recyclerView.setAdapter(adapter_requests);

        adapter_requests.setClickListener(new RequestsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               if(view.getId() == R.id.approve_group_btn)
               {
                   approveRequests(arrGroups.get(position));
                   Intent intent = new Intent(getApplicationContext(), NavActivity.class);
                   intent.putExtra("phoneNumber",LoggedInUserFromDB);
                   startActivity(intent);
                   finish();
               }
                else
                    cancelGroupRequest(arrGroups.get(position));
            }

        });
    }

    private void approveRequests(String adminPhone) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("group");

        CreateNewGroupActivity.updateMemberStatusInDB(LoggedInUserFromDB,Person.GROUP_STATUS.MEMBER);


        for (int i = 0; i < arrGroups.size(); i++)
        {
            String admin = arrGroups.get(i);
            if(!admin.equals(adminPhone))
            {
                reference.child(admin).child("groupMembers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            List<String> membersPhones = new ArrayList<>();
                            for (DataSnapshot snap: snapshot.getChildren())
                            {
                                if(!snap.getValue().equals(LoggedInUserFromDB))
                                {
                                    membersPhones.add(snap.getValue(String.class));

                                }
                            }
                            reference.child(admin).child("groupMembers").setValue(membersPhones);
                            if(admin.equals(arrGroups.get(arrGroups.size()-1)))
                            {
                                Intent intent = new Intent(getApplicationContext(), NavActivity.class);
                                intent.putExtra("phoneNumber",LoggedInUserFromDB);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        }

    }

    private void cancelGroupRequest(String adminPhone)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("group");

        reference.child(adminPhone).child("groupMembers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    List<String> groupMembers = new ArrayList<>();
                    groupMembers.add("");
                    for (DataSnapshot snap: snapshot.getChildren()) {
                        if(!snap.getValue().equals(LoggedInUserFromDB))
                        {
                            groupMembers.add(snap.getValue(String.class));
                        }
                    }
                    reference.child(adminPhone).child("groupMembers").setValue(groupMembers);
                    finish();
                    startActivity(getIntent());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setAdapter() {
        RequestsAdapter adapter = new RequestsAdapter(arrGroups);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
}
