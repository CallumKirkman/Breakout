package com.example.breakout;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.breakout.UserDBContract.*;
import com.spotify.protocol.types.ImageUri;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class SavingSongToDB extends AppCompatActivity {

    private static SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.account_creation);
        setContentView(R.layout.activity_create_account);



    }



    public static void saveLikedSong(String song_name, String artist_name, String song_uri, String image_uri) {

        //write to song table
        //write to user songs table with association
    }

    public static void saveDislikedSong(String song_name, int song_like, String artist_name, String song_uri, ImageUri image_uri) {


//
//        try {
//            mDatabase.insert(SongStorage.TABLE_NAME, null, cV);
//        }
//        catch (Error error) {
//            //return "Failed to like song";
//        }
    }


}
