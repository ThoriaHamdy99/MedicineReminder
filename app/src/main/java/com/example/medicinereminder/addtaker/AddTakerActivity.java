package com.example.medicinereminder.addtaker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.medicinereminder.R;

public class AddTakerActivity extends AppCompatActivity {
    Button btnAddTaker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_taker);
        btnAddTaker = findViewById(R.id.btnAddTaker);
        btnAddTaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}