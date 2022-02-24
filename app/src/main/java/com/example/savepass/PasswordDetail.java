package com.example.savepass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class PasswordDetail extends AppCompatActivity {
    private TextView platform;
    private TextView username;
    private TextView password;
    private ImageButton editButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_detail);

        platform=findViewById(R.id.platformText);
        username=findViewById(R.id.usernameText);
        password=findViewById(R.id.passwordText);
        editButton=findViewById(R.id.editButton);

        String platformT="";String usernameT="";String passwordT="";

        Intent current_intent=getIntent();
        platformT=current_intent.getStringExtra("platform");
        usernameT=current_intent.getStringExtra("username");
        passwordT=current_intent.getStringExtra("password");


        platform.setText(platformT);
        username.setText(usernameT);
        password.setText(passwordT);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),PasswordChange.class);
                intent.putExtra("user_uid",getIntent().getStringExtra("user_uid"));
                intent.putExtra("doc_id",getIntent().getStringExtra("doc_id"));
                v.getContext().startActivity(intent);
            }
        });


    }
}