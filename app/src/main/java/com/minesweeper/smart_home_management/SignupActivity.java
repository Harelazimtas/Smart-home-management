package com.minesweeper.smart_home_management;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import objects.Person;

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
                String name;
                String phone_Number;
                name = firstName.getText().toString() + " " + lastName.getText().toString();
                phone_Number = phoneNumber.getText().toString();
                person = new Person(name,  phone_Number);

                root.child(person.getPhoneNumber()).setValue(person);
                Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
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

}
