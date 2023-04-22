package com.example.holidaydestinationsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText emailInput;
    private Button signUpButton;
    private Button signInButton;
    private DbHelper dbHelper;

    private boolean isSigningUp;
    private String usernameToRegister;
    private String passwordToRegister;
    private String emailToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameInput = findViewById(R.id.signUpUsername);
        passwordInput = findViewById(R.id.signUpPassword);
        emailInput = findViewById(R.id.signUpEmail);
        signUpButton = findViewById(R.id.signUpSubmit);
        signInButton = findViewById(R.id.signUpToSignIn);

        dbHelper = DbHelper.getInstance(this);
        signUpButton.setOnClickListener(this::onClickSignUpButton);
        signInButton.setOnClickListener(this::onClickSignInButton);

        isSigningUp = false;
    }

    public void onClickSignInButton(View v) {
        this.startActivity(new Intent(this, SignInActivity.class));
    }

    public void onClickSignUpButton(View v) {
        if (isSigningUp)
            return;
        isSigningUp = true;

        usernameToRegister = usernameInput.getText().toString().trim();
        passwordToRegister = passwordInput.getText().toString().trim();
        emailToRegister = emailInput.getText().toString().trim();
        if (usernameToRegister.isEmpty() || passwordToRegister.isEmpty() || emailToRegister.isEmpty()) {
            Toast.makeText(this, "All fields must be completed", Toast.LENGTH_SHORT).show();
            isSigningUp = false;
            return;
        }

        // Verifying if user already exists. If it doesn't, create the new user.
        String checkUserQuery = "SELECT " + DbHelper.USERS_COL_ID +
                                " FROM " + DbHelper.USERS_TABLE +
                                " WHERE " + DbHelper.USERS_COL_USERNAME + "='" + usernameToRegister + "'";
        dbHelper.rawQueryAsync(checkUserQuery, (Cursor cursor) -> {
            if (cursor != null && cursor.moveToFirst()) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                isSigningUp = false;
            } else {
                String insertUserQuery = "INSERT INTO " + DbHelper.USERS_TABLE + " VALUES(NULL, '" +
                                          usernameToRegister + "', '" +
                                          passwordToRegister + "', '" +
                                          emailToRegister + "')";
                dbHelper.execSQLAsync(insertUserQuery, this::OnUserCreated);
            }
            if (cursor != null)
                cursor.close();
        });
    }

    public void OnUserCreated() {
        isSigningUp = false;
        Toast.makeText(this, "Account created", Toast.LENGTH_SHORT).show();
        this.startActivity(new Intent(this, SignInActivity.class));
    }
}