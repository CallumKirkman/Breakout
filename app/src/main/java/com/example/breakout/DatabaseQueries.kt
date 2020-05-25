package com.example.breakout

import androidx.appcompat.app.AppCompatActivity
import com.example.breakout.UserDBContract.*
import com.example.breakout.fragments.FavouriteFragment

class DatabaseQueries: AppCompatActivity() {

    public fun getCurrentUserID(): Int {
        val dbHelper = UserDBHelper(this)
        val mDatabase = dbHelper.writableDatabase
        var userID: Int? = null
        // Sorting the results.
        val query =
            "SELECT USER_ID FROM TBL_USERDATA, TBL_CURRENT WHERE USER_EMAIL_ADDRESS = CURRENT_USER_EMAIL"
        val c = mDatabase.rawQuery(query, null)
        c?.moveToFirst()
        userID = c!!.getInt(c.getColumnIndex(UserEntry.COLUMN_USER_ID))
        return userID
    }

    public fun getUsersLikedSongs() {
        //want to read all of the music data which the current user has disliked
        val dbHelper = UserDBHelper(this)
        val mDatabase = dbHelper.readableDatabase
        val projection = arrayOf(
            SongStorage.COLUMN_SONG_NAME,
            SongStorage.COLUMN_ARTIST_NAME,
            SongStorage.COLUMN_SONG_URI,
            SongStorage.COLUMN_IMAGE_URI
        )
        val selection =
            "${UserDBContract.UserSongs.COLUMN_FK_USER_ID} = ?  AND ${UserDBContract.UserSongs.COLUMN_FK_SONG_ID} = ?  AND ${UserDBContract.UserSongs.COLUMN_SONG_LIKE} = 1 AND ${UserEntry.COLUMN_USER_ID} like ?"
        val selectionArgs = arrayOf(
            UserEntry.COLUMN_USER_ID,
            SongStorage.COLUMN_SONG_ID,
            getCurrentUserID().toString()
        )
        val sortOrder = "${SongStorage.COLUMN_SONG_NAME} DESC"
        var songName: String
        var imageURI: String
        var songURI: String
        mDatabase.query(
            SongStorage.TABLE_NAME + ", " + UserEntry.TABLE_NAME + ", " + UserDBContract.UserSongs.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        ).use { cursor ->
            while (cursor.moveToNext()) {
                val itemID: Long
                songName = cursor.getString(cursor.getColumnIndex(SongStorage.COLUMN_SONG_NAME))
                imageURI = cursor.getString(cursor.getColumnIndex(SongStorage.COLUMN_IMAGE_URI))
                songURI = cursor.getString(cursor.getColumnIndex(SongStorage.COLUMN_SONG_URI))
//                FavouriteFragment().songName.add(songName)
//                FavouriteFragment().imageURI.add(imageURI)
//                FavouriteFragment().songURI.add(songURI)
            }
        }
    }
}