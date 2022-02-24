package com.example.savepass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpers.PasswordGenerator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class PasswordChange extends AppCompatActivity {
   private TextView password;
   private TextView passwordRe;
   private EditText number;
   private Switch auto;
   private ImageButton reveal;
   private ImageButton submit;
   private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        db=FirebaseFirestore.getInstance();
        password=findViewById(R.id.password_renew1);
        passwordRe=findViewById(R.id.password_renew2);
        number=findViewById(R.id.pw_num);
        auto=findViewById(R.id.switch2);
        reveal=findViewById(R.id.revealButton2);
        submit=findViewById(R.id.create_btn2);

        String user_uid=getIntent().getStringExtra("user_uid");
        String doc_id=getIntent().getStringExtra("doc_id");
        Log.d("uid",user_uid);
        Log.d("docid",doc_id);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().length()!=0 && passwordRe.getText().toString().length()!=0 && password.getText().toString().equals(passwordRe.getText().toString())) {

                   /* db.collection("all_users").document(user_uid).collection("pw_details").document(doc_id).update("password", password.getText(),
                    "published",new Date());*/
                    DocumentReference docRef = db.collection("all_users").document(user_uid).collection("pw_details").document(doc_id);
                    docRef
                            .update("password", password.getText().toString(),
                                    "published",new Timestamp(new Date()))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("update_hell","password is updated");
                                }
                            });
                }
                else{
                    Toast.makeText(PasswordChange.this, "Your password is not matching",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //make password uneditable
                    password.setEnabled(false);
                    passwordRe.setEnabled(false);
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
                    passwordRe.setText(pwd);
                }
                else{
                    password.setText("");
                    passwordRe.setText("");
                    password.setEnabled(true);
                    passwordRe.setEnabled(true);
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
    }
}