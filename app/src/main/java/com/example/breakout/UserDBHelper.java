package com.example.breakout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.breakout.UserDBContract.UserEntry;

import androidx.annotation.Nullable;

class UserDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BreakoutUsers.db";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                        UserEntry.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                        UserEntry.COLUMN_FORENAME + " TEXT," +
                        UserEntry.COLUMN_SURNAME + " TEXT," +
                        UserEntry.COLUMN_EMAIL_ADDRESS + " TEXT," +
                        UserEntry.COLUMN_PASSWORD + " TEXT," +
                        UserEntry.COLUMN_SALT + " TEXT);";


        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ UserEntry.TABLE_NAME);
    }




}
