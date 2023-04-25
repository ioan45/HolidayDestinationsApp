package com.example.holidaydestinationsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;

public class UserProfileActivity extends AppCompatActivity {
    private static final int SELECT_GALLERY_IMAGE = 200;

    private ImageView profileImage;
    private TextView greetingMsg;
    private Button signOutButton;

    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileImage = findViewById(R.id.profileImage);
        greetingMsg = findViewById(R.id.greetingMessage);
        signOutButton = findViewById(R.id.signOutButton);

        dbHelper = DbHelper.getInstance(this);

        initProfileImage();
        greetingMsg.setText(getString(R.string.greetings, User.signedInUser.getDisplayName()));
        signOutButton.setOnClickListener(this::onSignOutButtonPress);
    }

    private void initProfileImage() {
        String selectQuery = "SELECT " + DbHelper.USERS_COL_PROFILE_IMG +
                             " FROM " + DbHelper.USERS_TABLE +
                             " WHERE " + DbHelper.USERS_COL_ID + "=" + User.signedInUser.getId();
        dbHelper.rawQueryAsync(selectQuery, (Cursor cursor) -> {
            if (cursor.moveToFirst()) {
                int blobIndex = cursor.getColumnIndex(DbHelper.USERS_COL_PROFILE_IMG);
                if (blobIndex >= 0) {
                    byte[] blobImg = cursor.getBlob(blobIndex);
                    if (blobImg != null) {
                        Bitmap bitmapImg = BitmapFactory.decodeByteArray(blobImg, 0, blobImg.length);
                        profileImage.setImageBitmap(bitmapImg);
                    }
                }
            }
            cursor.close();
            profileImage.setOnClickListener(this::onClickProfilePicture);
        });
    }

    public void onClickProfilePicture(View v) {
        Intent newIntent = new Intent();
        newIntent.setType("image/*");
        newIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(newIntent, "Select Gallery Image"), SELECT_GALLERY_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_GALLERY_IMAGE) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    profileImage.setImageURI(selectedImageUri);
                    AsyncTask.execute(() -> {
                        // Saving the selected image to database
                        Bitmap selectedImg = ((BitmapDrawable)profileImage.getDrawable()).getBitmap();
                        saveProfileImageToDb(selectedImg);
                    });
                }
            }
        }
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
            // Cancelling the scheduled session expired notification.
            stopService(new Intent(this.getApplicationContext(), RaiseNotificationService.class));
            User.signedInUser = null;
            loadSignInActivity();
        }
    }

    private void saveProfileImageToDb(Bitmap profileImg) {
        String blobImg = bitmapToBlobLiteral(profileImg);
        String updateQuery = "UPDATE " + DbHelper.USERS_TABLE +
                             " SET " + DbHelper.USERS_COL_PROFILE_IMG + "=" + blobImg +
                             " WHERE " + DbHelper.USERS_COL_ID + "=" + User.signedInUser.getId();
        dbHelper.execSQLAsync(updateQuery, null);
    }

    private void loadSignInActivity() {
        this.startActivity(new Intent(this, SignInActivity.class));
    }

    private String bitmapToBlobLiteral(Bitmap img) {
        // Bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] byteArr = stream.toByteArray();
        // Byte array to blob literal
        StringBuilder stringBuilder = new StringBuilder(byteArr.length);
        for (byte byteChar : byteArr)
            stringBuilder.append(String.format("%02X", byteChar));
        return "X'" + stringBuilder + "'";
    }
}