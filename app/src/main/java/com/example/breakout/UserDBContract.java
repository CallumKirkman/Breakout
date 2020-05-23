package com.example.breakout;

import android.provider.BaseColumns;

class UserDBContract {

    private UserDBContract(){}//exists so no instance can be created

    public static final class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "TBL_USERDATA";
        public static final String COLUMN_ID = "USER_ID";
        public static final String COLUMN_FORENAME = "USER_FORENAME";
        public static final String COLUMN_SURNAME = "USER_SURNAME";
        public static final String COLUMN_EMAIL_ADDRESS = "USER_EMAIL_ADDRESS";
        public static final String COLUMN_PASSWORD = "USER_PASSWORD";
        public static final String COLUMN_SALT = "USER_SALT";
        public static final String COLUMN_SONG_ID = "FK_USER_SONGS";

    }


    public static final class SongStorage implements BaseColumns{
        public static final String TABLE_NAME = "TBL_SONG";
        public static final String COLUMN_ID = "SONG_ID";
        public static final String COLUMN_SONG_NAME = "SONG_NAME";
        public static final String COLUMN_SONG_LIKE = "SONG_LIKE";
        public static final String COLUMN_ARTIST_NAME = "ARTIST_NAME";
        public static final String COLUMN_SONG_URI = "SONG_URI";
        public static final String COLUMN_IMAGE_URI = "IMAGE_URI";


    }

    public static final class UserSongs implements BaseColumns{
        public static final String TABLE_NAME = "TBL_USER_SONGS";
        public static final String COLUMN_SONG_ID = "FK_USER_ID";
        public static final String COLUMN_USER_ID = "FK_SONG_ID";
       // public static final String COLUMN_SONG_LIKE = "SONG_LIKE";





    }


}
