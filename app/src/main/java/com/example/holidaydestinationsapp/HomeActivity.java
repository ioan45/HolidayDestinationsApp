package com.example.holidaydestinationsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class HomeActivity extends AppCompatActivity {
    private Button toProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toProfileButton = findViewById(R.id.toProfileButton);
        toProfileButton.setOnClickListener((View v) -> {
            this.startActivity(new Intent(this, UserProfileActivity.class));
        });
    }
}