package com.example.holidaydestinationsapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.UUID;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;

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
        findViewById(R.id.sign_in_button).setOnClickListener(this::onClickSignInGoogleButton);

        isSigningIn = false;
        passwordToCheck = null;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        User.googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Google user already signed in.
            User.signedInUser = new User(-1, account.getDisplayName());
            this.startActivity(new Intent(this, HomeActivity.class));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    public void onClickSignUpButton(View v) {
        this.startActivity(new Intent(this, SignUpActivity.class));
    }

    public void onClickSignInGoogleButton(View v) {
        Intent signInIntent = User.googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Google user signed in.
            User.signedInUser = new User(-1, account.getDisplayName());
            this.startActivity(new Intent(this, HomeActivity.class));
        } catch (ApiException e) {
            finish();
            startActivity(getIntent());
        }
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