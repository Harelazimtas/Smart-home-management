package com.minesweeper.smart_home_management;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.minesweeper.smart_home_management.model.Person;

public class LoginActivity extends AppCompatActivity {
    private Button signup_btn;
    private Button login_btn;
    private EditText login_txt;
    private String userPhoneNumber = "";
    private String nameFromDB;
    private String adminPhone;
    private  Group group;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_btn = (Button) findViewById(R.id.login_btn);
        signup_btn = (Button) findViewById(R.id.signup_btn);
        login_txt = findViewById(R.id.login_userName_TXT);



        login_btn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                if(validUser())
                {
                    retriveUser();
                    whatIsUserMemberStatus();

                }

            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener(){



            public void onClick(View v)
            {
                openSignupActivity();

            }
        });


    }


    private boolean validUser()
    {
        String loginUser =  typedPhone();
        if(loginUser.isEmpty())
        {
            login_txt.setError("Login user cannot be empty");
            return false;
        }
        else {
            login_txt.setError(null);
            return true;

        }
    }

    private void retriveUser()
    {
        String userName = typedPhone();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("subscribers");
        Query userFromDB = reference.orderByChild("phoneNumber").equalTo(userName);
        userFromDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {

                    String userNameFromDB = snapshot.child(userName).child("phoneNumber").getValue(String.class);

                    if(userNameFromDB.equals(userName))
                    {


                             nameFromDB = snapshot.child(userName).child("name").getValue(String.class);
                                 return;


                    }




                }
                login_txt.setError("No user was found");
                login_txt.requestFocus();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    private void whatIsUserMemberStatus()
    {
        String phone = typedPhone();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("subscribers");
        Query userFromDB = reference.orderByChild("phoneNumber").equalTo(phone);
        userFromDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status = "";
                for (DataSnapshot snap: snapshot.getChildren()) {
                    Log.d("status",snap.child("status").getValue().toString());

                    status = snap.child("status").getValue().toString();

                    switch(status)
                    {
                        case "NONE":
                            Intent intent = new Intent(getApplicationContext(), NoneUserAfterLoginActivity.class);
                            intent.putExtra("phoneNumber",typedPhone());
                            startActivity(intent);
                            Log.d("check", "CreatingNewGroupActivity");
                            break;
                        case "ADMIN":
                            Intent intentAdmin = new Intent(getApplicationContext(), TestActivity.class);
                            intentAdmin.putExtra("phoneNumber",typedPhone());
                            startActivity(intentAdmin);
                            Toast.makeText(getApplicationContext(), "Admin", Toast.LENGTH_LONG).show();
                            break;
                        case "MEMBER":
                            Intent intentMember = new Intent(getApplicationContext(), TestActivity.class);
                            intentMember.putExtra("phoneNumber",typedPhone());
                            startActivity(intentMember);
                            Toast.makeText(getApplicationContext(), "Member", Toast.LENGTH_LONG).show();
                            break;

                    }
                    return;

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void openSignupActivity()
    {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        Log.d("check", "inSignupActivity");
    }

    private String typedPhone()
    {
        String phoneFromUser = login_txt.getText().toString();
        return phoneFromUser;
    }




}
