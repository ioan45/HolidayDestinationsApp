package com.example.holidaydestinationsapp;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public class User {
    public static User signedInUser = null;
    public static GoogleSignInClient googleSignInClient = null;

    private final int id;
    private final String username;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
