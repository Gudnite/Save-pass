package com.example.savepass;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {

    ImageButton signup_btn;
    EditText username, email, password, repeatpw;
    private static FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();


        signup_btn = (ImageButton) findViewById(R.id.signup_btn);


        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        repeatpw = (EditText) findViewById(R.id.repeatpw);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username_str = username.getText().toString();
                String email_str = email.getText().toString();
                String password_str = password.getText().toString();
                String repeatpw_str = repeatpw.getText().toString();

                if (email_str.length() != 0 && password_str.length() != 0 && repeatpw_str.length() != 0 && username_str.length() != 0) {
                    if (password_str.equals(repeatpw_str)) {
                        signUp(email_str, password_str);
                    } else {
                        Toast.makeText(Signup.this, "Passwords are not matching",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Signup.this, "Fill all the fields",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Signup.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();


                        } else {
                            Toast.makeText(Signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
