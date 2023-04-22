package com.example.holidaydestinationsapp;

public class User {
    public static User signedInUser = null;

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
