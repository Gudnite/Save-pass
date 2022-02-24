package com.example.savepass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PasswordDeck extends AppCompatActivity {

private RecyclerView recyclerView;
private ImageButton createPwd;
private RecyclerView.Adapter recycleViewAdapter;
private RecyclerView.LayoutManager layoutManager;
private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_deck);
        String user_uid=getIntent().getStringExtra("uid");
        createPwd=findViewById(R.id.addPw);
        createPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(PasswordDeck.this,CreatePassword.class);
                intent.putExtra("uid",user_uid);
                startActivity(intent);
            }
        });

        db= FirebaseFirestore.getInstance();

        recyclerView=findViewById(R.id.recycle_view);
        getAllPasswords(user_uid);

        layoutManager=new LinearLayoutManager(this);



    }
    public void getAllPasswords(String user_uid) {
        ArrayList<QueryDocumentSnapshot> allPws=new ArrayList<>();
        db.collection("all_users").document(user_uid).collection("pw_details").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d("error1",user_uid);
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                allPws.add(document);
                                Log.d("error2",document.getId());
                                //time
                            }
                        } else {
                            Log.d("error_read","error while reading");
                        }
                        Log.d("error3",""+allPws.size());

                        recycleViewAdapter=new PasswordDeckAdapter(allPws,user_uid);
                        recyclerView.setAdapter(recycleViewAdapter);
                        recyclerView.setLayoutManager(layoutManager);

                    }
                });
    }
}