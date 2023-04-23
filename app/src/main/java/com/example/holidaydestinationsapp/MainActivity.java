package com.example.holidaydestinationsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Checking if user session exists.
        SharedPreferencesManager spManager = SharedPreferencesManager.getInstance(this);
        String sessionToken = spManager.getString(SharedPreferencesManager.KEY_SESSION_TOKEN);
        if (sessionToken == null) {
            this.startActivity(new Intent(this, SignInActivity.class));
        } else {
            DbHelper dbHelper = DbHelper.getInstance(this);
            String getUserQuery = "SELECT us." + DbHelper.USERS_COL_ID + ", us." + DbHelper.USERS_COL_USERNAME + ", ss." + DbHelper.SESSIONS_COL_START_DATE +
                                  " FROM " + DbHelper.USERS_TABLE + " us, " + DbHelper.SESSIONS_TABLE + " ss" +
                                  " WHERE us." + DbHelper.USERS_COL_ID + "= ss." + DbHelper.SESSIONS_COL_USER_ID +
                                    " and ss." + DbHelper.SESSIONS_COL_TOKEN + "='" + sessionToken + "'";
            dbHelper.rawQueryAsync(getUserQuery, this::OnGetUserTaskComplete);
        }
    }

    public void OnGetUserTaskComplete(Cursor cursor) {
        User user = null;

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("us." + DbHelper.USERS_COL_ID);
            int usernameIndex = cursor.getColumnIndex("us." + DbHelper.USERS_COL_USERNAME);
            int dateIndex = cursor.getColumnIndex("ss." + DbHelper.SESSIONS_COL_START_DATE);
            if (idIndex >= 0 && usernameIndex >= 0 && dateIndex >= 0) {
                int userId = cursor.getInt(idIndex);
                String username = cursor.getString(usernameIndex);
                long dateAsSecondsSinceEpoch = cursor.getLong(dateIndex);
                if (System.currentTimeMillis() / 1000 - dateAsSecondsSinceEpoch < 86400) {
                    // A session is valid for 1 day.
                    user = new User(userId, username, username);
                }
            }
        }
        cursor.close();

        if (user == null) {
            this.startActivity(new Intent(this, SignInActivity.class));
        } else {
            User.signedInUser = user;
            this.startActivity(new Intent(this, HomeActivity.class));
        }
    }
}