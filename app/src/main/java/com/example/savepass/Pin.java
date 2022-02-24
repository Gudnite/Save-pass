package com.example.savepass;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Pin extends AppCompatActivity {

    ImageButton back_btn, continue_btn;
    EditText pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        back_btn = (ImageButton)findViewById(R.id.back_btn);
        continue_btn = (ImageButton)findViewById(R.id.continue_btn);

        pin = (EditText)findViewById(R.id.pin);

    }
}
