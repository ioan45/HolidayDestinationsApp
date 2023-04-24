package com.example.holidaydestinationsapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

public class DbHelper extends SQLiteOpenHelper {
    public interface RawQueryCompleteCallback {
        void OnComplete(Cursor cursor);
    }

    public interface ExecSQLCompleteCallback {
        void OnComplete();
    }

    private static class RawQueryTask extends AsyncTask<String, Void, Cursor> {
        private RawQueryCompleteCallback callback;
        private SQLiteDatabase db;

        public RawQueryTask(RawQueryCompleteCallback callback, SQLiteDatabase db) {
            this.callback = callback;
            this.db = db;
        }

        protected Cursor doInBackground(String... query) {
           return db.rawQuery(query[0], null);
        }

        protected void onPostExecute(Cursor result) {
            if (callback != null)
                callback.OnComplete(result);
        }
    }

    private static class ExecSQLTask extends AsyncTask<String, Void, Void> {
        private ExecSQLCompleteCallback callback;
        private SQLiteDatabase db;

        public ExecSQLTask(ExecSQLCompleteCallback callback, SQLiteDatabase db) {
            this.callback = callback;
            this.db = db;
        }

        protected Void doInBackground(String... query) {
            db.execSQL(query[0]);
            return null;
        }

        protected void onPostExecute(Void nullResult) {
            if (callback != null)
                callback.OnComplete();
        }
    }

    public static final String DB_NAME = "holiday_destinations.db";
    public static final int DB_VERSION = 1;
    public static final String USERS_TABLE = "user";
    public static final String USERS_COL_ID = "id";
    public static final String USERS_COL_USERNAME = "username";
    public static final String USERS_COL_PASSWORD = "password";
    public static final String USERS_COL_EMAIL = "email";
    public static final String USERS_COL_PROFILE_IMG = "profile_img";
    public static final String SESSIONS_TABLE = "session";
    public static final String SESSIONS_COL_USER_ID = "user_id";
    public static final String SESSIONS_COL_TOKEN = "token";
    public static final String SESSIONS_COL_START_DATE = "start_date";

    private static DbHelper instance;

    public static synchronized DbHelper getInstance(Context context) {
        if (instance == null)
            instance = new DbHelper(context.getApplicationContext());
        return instance;
    }

    private DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersQuery = "CREATE TABLE " + USERS_TABLE + "(" +
                                   USERS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                   USERS_COL_USERNAME + " TEXT," +
                                   USERS_COL_PASSWORD + " TEXT, " +
                                   USERS_COL_EMAIL + " TEXT, " +
                                   USERS_COL_PROFILE_IMG + " BLOB)";
        String createSessionsQuery = "CREATE TABLE " + SESSIONS_TABLE + "(" +
                                      SESSIONS_COL_USER_ID + " INTEGER PRIMARY KEY," +
                                      SESSIONS_COL_TOKEN + " TEXT," +
                                      SESSIONS_COL_START_DATE + " INTEGER)";
        db.execSQL(createUsersQuery);
        db.execSQL(createSessionsQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SESSIONS_TABLE);
            onCreate(db);
        }
    }

    public void rawQueryAsync(String query, RawQueryCompleteCallback callback) {
        SQLiteDatabase db = this.getReadableDatabase();
        RawQueryTask task = new RawQueryTask(callback, db);
        task.execute(query);
    }

    public void execSQLAsync(String query, ExecSQLCompleteCallback callback) {
        SQLiteDatabase db = this.getWritableDatabase();
        ExecSQLTask task = new ExecSQLTask(callback, db);
        task.execute(query);
    }
}
