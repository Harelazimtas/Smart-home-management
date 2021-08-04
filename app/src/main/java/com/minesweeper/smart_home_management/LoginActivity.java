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

public class LoginActivity extends AppCompatActivity {
    private Button signup_btn;
    private Button login_btn;
    private EditText login_txt;
    private boolean userValidation;
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

                }
                else
                {

                    Toast.makeText(getApplicationContext(),"Invalid", Toast.LENGTH_LONG).show();
                }
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener(){



            public void onClick(View v)
            {
                openSignupActivity();

            }
        });
/*
        login_btn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v)
            {
                openSignupActivity();

            }
        });

 */

    }


    private boolean validUser()
    {
        String loginUser = login_txt.getText().toString();
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
        String userName = login_txt.getText().toString();
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
                        String nameFromDB = snapshot.child(userName).child("name").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), NavActivity.class);
                       intent.putExtra("name", nameFromDB);
                        startActivity(intent);
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

      //  login_txt


    }

    private void openSignupActivity()
    {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        Log.d("check", "inSignupActivity");
    }
}
