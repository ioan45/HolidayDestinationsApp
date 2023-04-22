package com.example.holidaydestinationsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;

public class SharedPreferencesManager {
    private static class PutStringTask extends AsyncTask<String, Void, Void> {
        private final SharedPreferences.Editor editor;

        public PutStringTask(SharedPreferences.Editor editor) {
            this.editor = editor;
        }

        protected Void doInBackground(String... pair) {
            editor.putString(pair[0], pair[1]);
            editor.commit();
            return null;
        }
    }

    public static final String KEY_SESSION_TOKEN = "session_token";

    private static final String FILE_NAME = "holiday_destinations";
    private static SharedPreferencesManager instance;

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null)
            instance = new SharedPreferencesManager(context.getApplicationContext());
        return instance;
    }

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putStringAsync(String key, String value) {
        (new PutStringTask(editor)).execute(key, value);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }
}
