package com.minesweeper.smart_home_management;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.minesweeper.smart_home_management.model.Person;

public class SignupActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private Button submit_btn;
    private Person person;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference("subscribers");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firstName = findViewById(R.id.firstNameEditText);
        firstName.setOnClickListener(this::onClick);
        lastName = findViewById(R.id.lastNameEditText);
        lastName.setOnClickListener(this::onClick);
        phoneNumber = findViewById(R.id.editTextPhone);
        phoneNumber.setOnClickListener(this::onClick);
        submit_btn = findViewById(R.id.sign_me_btn);

        submit_btn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                isPhoneInTheDB();

            }
        });

    }
    public void onClick(View v) {
        if(v.getId() == R.id.firstNameEditText)
        firstName.getText().clear();
        else if(v.getId() == R.id.lastNameEditText)
            lastName.getText().clear();
        else
        phoneNumber.getText().clear();
    }

    private void isPhoneInTheDB()
    {
        String subPhone = phoneNumber.getText().toString();
        String firsNames = firstName.getText().toString();
        String lastNames = lastName.getText().toString();

        if((!lastNames.equals(""))&&(!firsNames.equals("")))
        root.child(subPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists() && (!(phoneNumber.getText().toString().equals(""))  || !(phoneNumber.getText().toString().equals("Phone number"))))
                {
                    String name;
                    String phone_Number;
                    name = firstName.getText().toString() + " " + lastName.getText().toString();
                    phone_Number = phoneNumber.getText().toString();
                    person = new Person(name,  phone_Number);

                    root.child(person.getPhoneNumber()).setValue(person);
                    Intent intent = new Intent(getApplicationContext(), NoneUserAfterLoginActivity.class);
                    intent.putExtra("phoneNumber", phone_Number);
                    startActivity(intent);
                    finish();
                    return;


                }

                if(phoneNumber.getText().toString().equals(""))
                {
                    phoneNumber.setError("This field cannot be empty");
                    phoneNumber.requestFocus();
                    return;

                }
                else if(phoneNumber.getText().toString().equals("Phone number"))
                {
                    phoneNumber.setError("Please type any phone number");
                    phoneNumber.requestFocus();
                    return;

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        else{
            if(firstName.getText().toString().equals(""))
            {
                firstName.setError("This field cannot be empty");
                firstName.requestFocus();
                return;
            }
            else if(lastName.getText().toString().equals(""))
            {
                lastName.setError("This field cannot be empty");
                lastName.requestFocus();
                return;
            }

            else
            {
                phoneNumber.setError("This user is already a subscriber");
                phoneNumber.requestFocus();
                return;

            }
        }
    }

}
