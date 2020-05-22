package com.example.breakout;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.breakout.UserDBContract.*;



class UserDBHelper extends SQLiteOpenHelper {


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;//DO NOT CHANGE
    public static final String DATABASE_NAME = "BreakoutUsersDB.db";

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
                ;
        final String SQL_CREATE_SONG_ENTRIES =
                "CREATE TABLE " + SongStorage.TABLE_NAME + " (" +
                        SongStorage.COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        SongStorage.COLUMN_SONG_NAME + " TEXT," +
                        SongStorage.COLUMN_SONG_LIKE + " INTEGER," +
                        SongStorage.COLUMN_ARTIST_NAME + " TEXT," +
                        SongStorage.COLUMN_SONG_URI + " TEXT," +
                        SongStorage.COLUMN_IMAGE_URI + " TEXT);";

        final String SQL_CREATE_USER_SONGS_ENTRIES =
                "CREATE TABLE " + UserSongs.TABLE_NAME + " (" +
                        UserSongs.COLUMN_USER_ID + " INTEGER NOT NULL ," +
                        UserSongs.COLUMN_SONG_ID + " INTEGER NOT NULL ,"+
                        "PRIMARY KEY ( "+ UserSongs.COLUMN_USER_ID + ", " +  UserSongs.COLUMN_SONG_ID +")," +
                        "FOREIGN KEY (" + UserSongs.COLUMN_USER_ID + ") REFERENCES " + UserEntry.TABLE_NAME + " ( " + UserEntry.COLUMN_ID + "), "+
                        "FOREIGN KEY (" + UserSongs.COLUMN_SONG_ID + ") REFERENCES " + SongStorage.TABLE_NAME + " ( " + SongStorage.COLUMN_ID + ")" +
                        ");";





        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_SONG_ENTRIES);
        db.execSQL(SQL_CREATE_USER_SONGS_ENTRIES);
        db.execSQL("COMMIT;");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SongStorage.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserSongs.TABLE_NAME);

    }


}
