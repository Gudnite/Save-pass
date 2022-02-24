package com.example.savepass;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    ImageButton login_btn;
    private static EditText username, password;
    TextView forgotpw_btn;
    private static FirebaseAuth mAuth;

    Dialog builder1;
    ImageButton ok,cancel;
    EditText resetMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        login_btn = (ImageButton)findViewById(R.id.login_btn);

        forgotpw_btn = (TextView)findViewById(R.id.forgotpw_btn);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=Login.username.getText().toString();
                String password= Login.password.getText().toString();
                logIn(email,password);
            }
        });

        //------------Forgot Password-----------------
        forgotpw_btn = findViewById(R.id.forgotpw_btn);
        forgotpw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogbox(v);
            }
        });

    }
    public  void logIn(String email,String password){
        if(email.length()!=0&&password.length()!=0){
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        if(user!=null){
                            String uid = user.getUid();
                            Intent intent=new Intent(Login.this,PasswordDeck.class);
                            intent.putExtra("uid",uid);
                            startActivity(intent);
                        }

                    }
                    else{
                        Toast.makeText(Login.this, "Your Email or Password is incorrect",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(Login.this, "Your Email or Password is incorrect",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void dialogbox(View v){
        builder1 = new Dialog(this);
        builder1.show();
        LayoutInflater customInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customLayout=customInflater.inflate(R.layout.dialog_box, (ViewGroup) findViewById(R.id.root));
        builder1.setContentView(customLayout);

        resetMail = (EditText) customLayout.findViewById(R.id.re_pwd);

        ok=(ImageButton)customLayout.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = resetMail.getText().toString();
                if(mail.length()!=0) {
                    mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Login.this, "Reset link send to your email.", Toast.LENGTH_SHORT).show();
                            builder1.dismiss();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Login.this, "Error! Reset link is not send." + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
                else{
                    Toast.makeText(Login.this, "Enter valid email.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancel=(ImageButton)customLayout.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //done what do you want to do
                builder1.dismiss();
            }
        });
    }
}