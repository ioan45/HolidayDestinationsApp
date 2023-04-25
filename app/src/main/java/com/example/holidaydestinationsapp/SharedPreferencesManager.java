package com.example.holidaydestinationsapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;

public class SharedPreferencesManager {
    public interface OnCompleteCallback {
        void OnComplete();
    }

    private static class PutStringTask extends AsyncTask<String, Void, Void> {
        private final SharedPreferences.Editor editor;
        private OnCompleteCallback callback;

        public PutStringTask(SharedPreferences.Editor editor, OnCompleteCallback callback) {
            this.editor = editor;
            this.callback = callback;
        }

        protected Void doInBackground(String... pair) {
            editor.putString(pair[0], pair[1]);
            editor.commit();
            return null;
        }

        protected void onPostExecute(Void nothing) {
            if (callback != null)
                callback.OnComplete();
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
        (new PutStringTask(editor, null)).execute(key, value);
    }

    public void putStringAsync(String key, String value, OnCompleteCallback callback) {
        (new PutStringTask(editor, callback)).execute(key, value);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }
}
