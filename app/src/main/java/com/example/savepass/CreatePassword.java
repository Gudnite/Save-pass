package com.example.savepass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpers.*;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;

public class CreatePassword extends AppCompatActivity {
private TextView platform;
private TextView username;
private EditText password;
private EditText number;
private Switch auto;
private ImageButton createButton;
private ImageButton backButton;
private ImageButton reveal;
private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        platform=findViewById(R.id.platform_new);
        username=findViewById(R.id.username_new);
        password=findViewById(R.id.password_new);
        number=findViewById(R.id.pw_num);
        auto=findViewById(R.id.switch1);
        createButton=findViewById(R.id.create_btn);
        reveal=findViewById(R.id.revealButton);

        db= FirebaseFirestore.getInstance();

        String user_uid=getIntent().getStringExtra("uid");



        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){
                    //make password uneditable
                    password.setEnabled(false);
                    //auto generate a password
                    int pw_number = Integer.parseInt(number.getText().toString());
                    PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                            .useDigits(true)
                            .useLower(true)
                            .useUpper(true)
                            .usePunctuation(true)
                            .build();
                    String pwd = passwordGenerator.generate(pw_number);
                    password.setText(pwd);

                }
                else{
                    password.setEnabled(true);
                }

            }
        });

        reveal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String platform_str=platform.getText().toString();
                String username_str=username.getText().toString();
                String pwd=password.getText().toString();

                if(platform_str.length()!=0 && username_str.length()!=0&&pwd.length()!=0){
                    HashMap<String,Object> pw_detail=new HashMap<>();
                    pw_detail.put("password",pwd);
                    pw_detail.put("Username",username_str);
                    pw_detail.put("platform",platform_str);
                    pw_detail.put("published",new Timestamp(new Date()));
                    Toast.makeText(CreatePassword.this, "Password is saving",
                            Toast.LENGTH_SHORT).show();
                   // backButton.setEnabled(false);
                    createButton.setEnabled(false);
                    db.collection("all_users").document(user_uid).collection("pw_details").add(pw_detail).addOnSuccessListener(
                            new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //Log.d("success_write", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    platform.setText("");username.setText("");password.setText("");
                                    backButton.setEnabled(true);
                                    createButton.setEnabled(true);
                                    Toast.makeText(CreatePassword.this, "Password is saved",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                    ).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("error4", "Error adding document", e);
                        }
                    });
                }
            }
        });

    }

}