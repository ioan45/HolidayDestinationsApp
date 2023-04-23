package com.example.holidaydestinationsapp;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class User {
    public static User signedInUser = null;
    public static GoogleSignInClient googleSignInClient = null;

    private final int id;
    private final String username;
    private final String displayName;

    public User(int id, String username, String displayName) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getDisplayName() { return displayName; }
}
