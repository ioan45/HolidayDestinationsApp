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
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        signOutButton = findViewById(R.id.homeSignOutButton);
        signOutButton.setOnClickListener(this::onSignOutButtonPress);
    }

    public void onSignOutButtonPress(View v) {
        if (!User.signedInUser.getUsername().equals(User.signedInUser.getDisplayName())) {
            // Signed in using google account.
            User.googleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            User.signedInUser = null;
                            loadSignInActivity();
                        }
                    });
        } else {
            // Signed in using app functionality.
            SharedPreferencesManager spManager = SharedPreferencesManager.getInstance(this);
            spManager.putStringAsync(SharedPreferencesManager.KEY_SESSION_TOKEN, null);
            User.signedInUser = null;
            loadSignInActivity();
        }
    }

    private void loadSignInActivity() {
        this.startActivity(new Intent(this, SignInActivity.class));
    }
}