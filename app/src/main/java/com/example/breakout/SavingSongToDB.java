package com.example.breakout;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.breakout.UserDBContract.*;


class SavingSongToDB extends AppCompatActivity {


    private static SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.account_creation);
        setContentView(R.layout.activity_create_account);

        // TODO: Add popup for viewing password constraints.
        UserDBHelper dbHelper = new UserDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();


    }

    public static void saveLikedSong(String song_name, int song_like, String artist_name, String song_uri, String image_uri ) {




        ContentValues cV = new ContentValues();
        cV.put(SongStorage.COLUMN_SONG_NAME, song_name);
        cV.put(SongStorage.COLUMN_SONG_LIKE, song_like);
        cV.put(SongStorage.COLUMN_ARTIST_NAME, artist_name);
        cV.put(SongStorage.COLUMN_SONG_URI, song_uri);
        cV.put(SongStorage.COLUMN_IMAGE_URI, image_uri);


       // mDatabase.insert(UserDBContract.UserEntry.TABLE_NAME, null, cV);


    }

    public static void saveDislikedSong() {


    }


}
