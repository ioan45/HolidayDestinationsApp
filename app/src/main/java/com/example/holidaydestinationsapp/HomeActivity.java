package com.example.holidaydestinationsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        signOutButton = findViewById(R.id.homeSignOutButton);
        signOutButton.setOnClickListener(this::onSignOutButtonPress);
    }

    public void onSignOutButtonPress(View v) {
        SharedPreferencesManager spManager = SharedPreferencesManager.getInstance(this);
        spManager.putStringAsync(SharedPreferencesManager.KEY_SESSION_TOKEN, null);
        User.signedInUser = null;

        this.startActivity(new Intent(this, SignInActivity.class));
    }
}