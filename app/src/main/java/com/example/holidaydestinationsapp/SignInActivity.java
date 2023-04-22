package com.example.holidaydestinationsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.UUID;

public class SignInActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText passwordInput;
    private Button signInButton;
    private Button signUpButton;
    private DbHelper dbHelper;
    private SharedPreferencesManager spManager;

    private boolean isSigningIn;
    private String usernameToCheck;
    private String passwordToCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        usernameInput = findViewById(R.id.signInUsername);
        passwordInput = findViewById(R.id.signInPassword);
        signInButton = findViewById(R.id.signInSubmit);
        signUpButton = findViewById(R.id.signInToSignUp);

        dbHelper = DbHelper.getInstance(this);
        spManager = SharedPreferencesManager.getInstance(this);
        signInButton.setOnClickListener(this::onClickSignInButton);
        signUpButton.setOnClickListener(this::onClickSignUpButton);

        isSigningIn = false;
        passwordToCheck = null;
    }

    public void onClickSignUpButton(View v) {
        this.startActivity(new Intent(this, SignUpActivity.class));
    }

    public void onClickSignInButton(View v) {
        if (isSigningIn)
            return;
        isSigningIn = true;

        usernameToCheck = usernameInput.getText().toString().trim();
        passwordToCheck = passwordInput.getText().toString().trim();
        if (usernameToCheck.isEmpty() || passwordToCheck.isEmpty()) {
            Toast.makeText(this, "All fields must be completed", Toast.LENGTH_SHORT).show();
            isSigningIn = false;
            return;
        }

        // Trying to get the user from database.
        String selectQuery = "SELECT " + DbHelper.USERS_COL_ID +
                             " FROM " + DbHelper.USERS_TABLE +
                             " WHERE " + DbHelper.USERS_COL_USERNAME + "='" + usernameToCheck + "'" +
                                " and " + DbHelper.USERS_COL_PASSWORD + "='" + passwordToCheck + "'";
        dbHelper.rawQueryAsync(selectQuery, this::onGetUserComplete);
    }

    private void onGetUserComplete(Cursor cursor) {
        User user = null;

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DbHelper.USERS_COL_ID);
            if (idIndex >= 0) {
                int userId = cursor.getInt(idIndex);
                user = new User(userId, usernameToCheck);
            }
        }
        if (cursor != null)
            cursor.close();

        isSigningIn = false;
        if (user == null) {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        } else {
            User.signedInUser = user;
            createUserSession(user.getId());
            this.startActivity(new Intent(this, HomeActivity.class));
        }
    }

    private void createUserSession(int userId) {
        String newSessionToken = generateSessionToken();

        // Adding the token to SharedPreferences.
        spManager.putStringAsync(SharedPreferencesManager.KEY_SESSION_TOKEN, newSessionToken);

        // Adding the user session in database (firstly, old session of this user is deleted).
        String deleteQuery = "DELETE FROM " + DbHelper.SESSIONS_TABLE +
                             " WHERE " + DbHelper.SESSIONS_COL_USER_ID + "=" + userId;
        String insertQuery = "INSERT INTO " + DbHelper.SESSIONS_TABLE + " VALUES(" +
                             userId + ", '" + newSessionToken + "', " + "strftime('%s','now'))";
        dbHelper.execSQLAsync(deleteQuery, () ->
            dbHelper.execSQLAsync(insertQuery, null)
        );
    }

    private String generateSessionToken() {
        return UUID.randomUUID().toString();
    }
}