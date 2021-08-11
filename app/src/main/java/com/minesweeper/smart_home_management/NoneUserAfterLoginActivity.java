package com.minesweeper.smart_home_management;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NoneUserAfterLoginActivity extends AppCompatActivity {
    private String LoggedInUserFromDB;
    private TextView loggedInUser;
    private Button createGroup;
    String LoggedUser = "";
    private Button approvereq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_noneuserafterlogin);

        Intent intent = getIntent();
        LoggedInUserFromDB = intent.getStringExtra("name");
        loggedInUser = findViewById(R.id.nameFromDB);

        loggedInUser.setText(LoggedInUserFromDB);

        createGroup = (Button) findViewById(R.id.new_group_btn);
        approvereq = (Button) findViewById(R.id.approve_req_btn);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoggedUser = intent.getStringExtra("phoneNumber");
               Intent intentsec = new Intent(getApplicationContext(), CreateNewGroupActivity.class);
               intentsec.putExtra("phoneNumber", LoggedUser);
                startActivity(intentsec);

            }
        });

        approvereq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoggedUser = intent.getStringExtra("phoneNumber");
                Intent intentsec = new Intent(getApplicationContext(), RequestApproveActivity.class);
                intentsec.putExtra("phoneNumber", LoggedUser);
                startActivity(intentsec);
            }
        });

    }

}
