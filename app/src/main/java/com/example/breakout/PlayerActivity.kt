package com.example.breakout

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.transition.Slide
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.breakout.AppCurrency.Companion.globalCurrency
import com.example.breakout.fragments.GENRE
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.ImageUri
import com.spotify.protocol.types.Track
import kotlinx.android.synthetic.main.activity_player.*
import com.example.breakout.UserDBContract.UserSongs
import com.example.breakout.UserDBContract.UserEntry
import com.example.breakout.UserDBContract.SongStorage


class PlayerActivity : AppCompatActivity() {

    private var popupView: View ? = null

    private var totalCurrency: Int = 0

    // Spotify connect
    private var spotifyAppRemote: SpotifyAppRemote? = null

    private val clientId = "daa95815630947bd980906b32437654d"
    private val redirectUri = "com.example.breakout:/callback"

    override fun onStart() {
        super.onStart()

        val connectionParams =
            ConnectionParams.Builder(clientId)
                .setRedirectUri(redirectUri)
                .showAuthView(true)
                .build()

        SpotifyAppRemote.connect(this, connectionParams, object : ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("PlayerActivity", "Connected! Yay!")
                connected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("PlayerActivity", throwable.message, throwable)
                onStop()
            }
        })
    }

    private fun connected() {
        spotifyAppRemote!!.playerApi.setShuffle(true)
        // Play a track
        //spotifyAppRemote!!.playerApi.play("spotify:album:3T4tUhGYeRNVUGevb0wThu")
        //Thread.sleep(50)
        //spotifyAppRemote!!.playerApi.pause()

        // User will be given a pre-selected (or random) song for music preference, in case new
        // If they look up a song, or have previous preference data those songs will be used

        songInfo()

        val genre = intent.getStringExtra(GENRE)

        if (genre != null) {
            //Play genre
            playGenre(genre)
        }
    }

    override fun onStop() {
        super.onStop()
        // Handle errors
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }

        val inflater = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        val popupView = inflater.inflate(R.layout.popup_spotify_disconnected, null)

        val display = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(display)
        // Popup is 90% screen width, and 40% screen height.
        val popupWidth = (display.widthPixels.toDouble() * 0.90).toInt()
        val popupHeight = (display.heightPixels.toDouble() * 0.40).toInt()

        val popupWindow = PopupWindow(popupView, popupWidth, popupHeight, true)


        popupWindow.elevation = 10.0F

        // Create a new slide animation for popup window enter transition
        val slideIn = Slide()
        slideIn.slideEdge = Gravity.TOP
        popupWindow.enterTransition = slideIn

        // Slide animation for popup window exit transition
        val slideOut = Slide()
        slideOut.slideEdge = Gravity.BOTTOM
        popupWindow.exitTransition = slideOut

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0)

        popupView?.findViewById<Button>(R.id.backToLogin)?.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    private var songName: String = ""
    private var artistName: String = ""
    private var albumCover: String = ""
    private var length: String = ""
    private lateinit var imageUri: ImageUri
    private var trackLink: String = ""

    // Song information
    private fun songInfo() {
        spotifyAppRemote!!.playerApi.subscribeToPlayerState().setEventCallback {
            val track: Track = it.track
            songName = track.name.toString()
            artistName = track.artist.name.toString()
            albumCover = track.album.toString()
            length = track.duration.toString()
            imageUri = track.imageUri
            trackLink = track.uri
        }
    }

    private fun albumImage() {
        // Get image from track
        spotifyAppRemote!!.imagesApi.getImage(imageUri, Image.Dimension.LARGE)
            .setResultCallback { bitmap: Bitmap ->
                albumView.setImageBitmap(bitmap)
            }
    }


    // Song control
    private var playPause = false

    fun playButtonClick(view: View) {
        if (playPause) {
            // Stop
            playPause = false
            playButton.setBackgroundResource(R.drawable.play)
            spotifyAppRemote!!.playerApi.pause()
        } else {
            // Start
            playPause = true
            playButton.setBackgroundResource(R.drawable.pause)
            spotifyAppRemote!!.playerApi.resume()

            // Get song data
            songInfo()
            albumImage()

            val songText: TextView = findViewById(R.id.textSongName)
            songText.text = songName

            val artistText: TextView = findViewById(R.id.textAtristName)
            artistText.text = artistName
        }
    }

    fun skipButtonClick(view: View) {
        if (globalCurrency < 5) {
            val toast = Toast.makeText(this, "Less than 5 vinyls \n Purchase more from store", Toast.LENGTH_SHORT)
            val v = toast.view.findViewById<View>(android.R.id.message) as TextView
            v.gravity = Gravity.CENTER
            toast.show()
        }
        else {
            globalCurrency -= 5
            // Skip song
            spotifyAppRemote!!.playerApi.skipNext()

            // Get song data
            songInfo()
            albumImage()

            val songText: TextView = findViewById(R.id.textSongName)
            songText.text = songName

            val artistText: TextView = findViewById(R.id.textAtristName)
            artistText.text = artistName

            playPause = true
            playButtonClick(playButton)

            removeSkips()
        }
    }

    fun previousButtonClick(view: View) {
        if (globalCurrency < 5) {
            val toast = Toast.makeText(this, "Less than 5 vinyls \n Purchase more from store", Toast.LENGTH_SHORT)
            val v = toast.view.findViewById<View>(android.R.id.message) as TextView
            v.gravity = Gravity.CENTER
            toast.show()
        }
        else {
            globalCurrency -= 5
            // Previous song
            spotifyAppRemote!!.playerApi.skipPrevious()

            // Get song data
            songInfo()
            albumImage()

            val songText: TextView = findViewById(R.id.textSongName)
            songText.text = songName

            val artistText: TextView = findViewById(R.id.textAtristName)
            artistText.text = artistName

            playPause = true
            playButtonClick(playButton)

            removeSkips()
        }
    }

    fun likeButtonClick(view: View) {
        val likeButton: ImageView = findViewById(R.id.likeButton)
        likeButton.setBackgroundResource(R.drawable.like_button_flash);
        val likeAnimation = likeButton.background as AnimationDrawable?
        likeAnimation?.start()
        // If not added?
        // Add to favourite songs
        //spotifyAppRemote!!.userApi.addToLibrary(trackLink)
    }

    fun dislikeButtonClick(view: View) {
        val dislikeButton: ImageView = findViewById(R.id.dislikeButton)
        dislikeButton.setBackgroundResource(R.drawable.dislike_button_flash);
        val dislikeAnimation = dislikeButton.background as AnimationDrawable?
        dislikeAnimation?.start()
        // Remove from play list?
        //spotifyAppRemote!!.userApi.removeFromLibrary(trackLink)
    }

    private fun removeSkips() {

        // ToDo(Push total to database)

        updateCurrency()

        val skip = findViewById<ImageButton>(R.id.skipButton)
        val prev = findViewById<ImageButton>(R.id.previousButton)

        if (globalCurrency < 5) {
            skip.setBackgroundResource(R.drawable.gray_forward)
            prev.setBackgroundResource(R.drawable.gray_rewind)
        }
        else {
            skip.setBackgroundResource(R.drawable.forward)
            prev.setBackgroundResource(R.drawable.rewind)
        }
    }


    private fun updateCurrency() {
        val currency = findViewById<TextView>(R.id.playerCurrency)
        totalCurrency = globalCurrency
        currency.text = totalCurrency.toString()
    }


    //Database stuff
    private fun checkSongExists(songName: String, songLink: String): Boolean {
        val dbHelper = UserDBHelper(this)
        val mDatabase = dbHelper.readableDatabase
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(SongStorage.COLUMN_SONG_NAME)
        // Filter results WHERE "title" = 'My Title'
        val selection =
            "${SongStorage.COLUMN_SONG_NAME} like ? AND " + "${SongStorage.COLUMN_SONG_URI} LIKE ?"
        val selectionArgs = arrayOf(songName, songLink)
        // How you want the results sorted in the resulting Cursor
        val sortOrder = "${SongStorage.COLUMN_SONG_NAME} DESC"
        val itemsIds: MutableList<String> = ArrayList<String>()
        val cursor = mDatabase.query(
            SongStorage.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        while (cursor.moveToNext()) {
            val itemID =
                cursor.getString(cursor.getColumnIndexOrThrow(SongStorage.COLUMN_SONG_NAME))
            itemsIds.add(itemID)
        }
        return itemsIds.size > 0
    }

    private fun writeBalanceToDB(balance: Int) {
        val dbHelper = UserDBHelper(this)
        val mDatabase = dbHelper.writableDatabase
        val cV = ContentValues().apply {
            put(UserEntry.COLUMN_USER_BALANCE, balance)
        }
        var currUsr = getCurrentUserID()
        val selection = "${UserEntry.COLUMN_USER_ID}  = " + currUsr
        mDatabase.update(UserEntry.TABLE_NAME, cV, selection, null)
    }

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

    private fun writeTOUserSongsDB(userID: Int, songId: Int, liked: Int) {
        val dbHelper = UserDBHelper(this)
        val mDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(UserSongs.COLUMN_FK_USER_ID, userID)
            put(UserSongs.COLUMN_FK_SONG_ID, songId)
            put(UserSongs.COLUMN_SONG_LIKE, liked)
        }
        mDatabase?.insert(UserSongs.TABLE_NAME, null, values)

    }

    private fun writeSongToDB() {
        val dbHelper = UserDBHelper(this)
        val mDatabase = dbHelper.writableDatabase
        val cV = ContentValues()
        cV.put(SongStorage.COLUMN_SONG_NAME, songName)
        cV.put(SongStorage.COLUMN_ARTIST_NAME, artistName)
        cV.put(SongStorage.COLUMN_SONG_URI, trackLink)
        cV.put(SongStorage.COLUMN_IMAGE_URI, imageUri.raw)
        if (!checkSongExists(songName, trackLink)) {
            mDatabase.insert(SongStorage.TABLE_NAME, null, cV)
        } else {
            //check if song already is liked by user
            //isLiked()
            Toast.makeText(this@PlayerActivity, "song has already been added", Toast.LENGTH_LONG)
        }
    }

    private fun getSongID(songName: String, songLink: String): Int {
        val dbHelper = UserDBHelper(this)
        val mDatabase = dbHelper.readableDatabase
        val projection = arrayOf(SongStorage.COLUMN_SONG_ID)
        val selection =
            "${SongStorage.COLUMN_SONG_NAME} LIKE ? AND " + "${SongStorage.COLUMN_SONG_URI} LIKE ?"
        val selectionArgs = arrayOf(songName, songLink)
        val sortOrder = "${SongStorage.COLUMN_SONG_NAME} DESC"

        val cursor = mDatabase.query(
            SongStorage.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        var songID: Int = -1
        while (cursor.moveToNext()) {
            val itemID =
                cursor.getInt(cursor.getColumnIndexOrThrow(SongStorage.COLUMN_SONG_ID))
            songID = itemID
        }
        return songID
    }

    private fun writeToUserSongDB(liked: Int) {
        val dbHelper = UserDBHelper(this)
        val mDatabase = dbHelper.readableDatabase
        var songID = getSongID(songName, trackLink)
        var userID = getCurrentUserID()
        try {
            if (liked == 1)//user has liked the song
            {
                //check if user has disliked the SONG if not like the song
                // else update song status to liked
                if (checkUserHasDislikedSong()) {//check if song has been disliked
                    //change disliked song to liked
                    val cV = ContentValues().apply {
                        put(UserSongs.COLUMN_SONG_LIKE, liked)
                    }
                    var currUsr = getCurrentUserID()
                    val selection = "${UserSongs.COLUMN_FK_USER_ID}  LIKE ? " + "${UserSongs.COLUMN_FK_SONG_ID} LIKE ?"
                    val selectionArgs = arrayOf(currUsr, songID)
                    mDatabase.update(
                        UserSongs.COLUMN_SONG_LIKE,
                        cV,
                        selection,
                        null)
                } else {//write song liked to db
                    if (userID != null) {
                        writeTOUserSongsDB(userID, songID, 1)
                    }
                }
            } else if (liked == 0) {
                //check if user has liked the song if not dislike the song.
                // else update song status disliked
                if (checkUserHasLikedSong()) {//check if song has been liked
                    //change liked song to disliked
                    val cV = ContentValues().apply {
                        put(UserSongs.COLUMN_SONG_LIKE, liked)
                    }
                    var currUsr = getCurrentUserID()
                    val selection = "${UserSongs.COLUMN_FK_USER_ID}  LIKE ? " + "${UserSongs.COLUMN_FK_SONG_ID} LIKE ?"
                    val selectionArgs = arrayOf(currUsr, songID)
                    mDatabase.update(
                        UserSongs.COLUMN_SONG_LIKE,
                        cV,
                        selection,
                        null)

                } else {
                    writeTOUserSongsDB(userID, songID, 0)
                }

            }
        }//error catching - if try doesn't work user has already liked song
        finally {
        }
    }

    private fun checkUserHasLikedSong(): Boolean//return true if user has liked the song
    {
        val dbHelper = UserDBHelper(this)
        val mDatabase = dbHelper.writableDatabase
        // Query Building - specify column, condition, sort order, etc.
        // Query Building - specify column, condition, sort order, etc.
        val projection = arrayOf(SongStorage.COLUMN_SONG_NAME)
        val selection =
            UserSongs.COLUMN_FK_USER_ID + " LIKE ? AND " + UserSongs.COLUMN_FK_SONG_ID + " LIKE ? AND " +
                    UserSongs.COLUMN_SONG_LIKE + " = 1 AND " + UserEntry.COLUMN_USER_ID + " = ? AND " +
                    SongStorage.COLUMN_SONG_ID + " LIKE ? "
        val selectionArgs = arrayOf<String>(UserEntry.COLUMN_USER_ID, SongStorage.COLUMN_SONG_ID, getCurrentUserID().toString(), getSongID(songName, trackLink).toString() )
        val sortOrder = UserSongs.COLUMN_SONG_LIKE + " DESC"
        val itemsIds: MutableList<Long> =
            java.util.ArrayList()
        mDatabase.query(
            UserEntry.TABLE_NAME + ", " + SongStorage.TABLE_NAME + ", " + UserSongs.TABLE_NAME,  // Table to query
            projection,  // The array of columns to return
            selection,  // The columns for the WHERE clause
            selectionArgs,  // The values for the WHERE clause
            null,  // Don't group the rows
            null,  // Don't filter by the row groups
            sortOrder
        ).use { cursor ->                            // Order to sort
            while (cursor.moveToNext()) {
                val itemID: Long =
                    cursor.getLong(cursor.getColumnIndex(UserEntry.COLUMN_EMAIL_ADDRESS))
                itemsIds.add(itemID)
            }
        }
        // Greater than 0, the email exists - true.
        // Greater than 0, the email exists - true.
        return itemsIds.size > 0
    }

    private fun checkUserHasDislikedSong(): Boolean// return true if user has disliked the song
    {
        val dbHelper = UserDBHelper(this)
        val mDatabase = dbHelper.writableDatabase
        // Query Building - specify column, condition, sort order, etc.
        // Query Building - specify column, condition, sort order, etc.
        val projection = arrayOf(SongStorage.COLUMN_SONG_NAME)
        val selection =
            UserSongs.COLUMN_FK_USER_ID + " LIKE ? AND " + UserSongs.COLUMN_FK_SONG_ID + " LIKE ? AND " +
                    UserSongs.COLUMN_SONG_LIKE + " = 0 AND " + UserEntry.COLUMN_USER_ID + " = ? AND " +
                    SongStorage.COLUMN_SONG_ID + " LIKE ? "
        val selectionArgs = arrayOf<String>(UserEntry.COLUMN_USER_ID, SongStorage.COLUMN_SONG_ID, getCurrentUserID().toString(), getSongID(songName, trackLink).toString() )
        val sortOrder = UserSongs.COLUMN_SONG_LIKE + " DESC"
        val itemsIds: MutableList<Long> =
            java.util.ArrayList()
        mDatabase.query(
            UserEntry.TABLE_NAME + ", " + SongStorage.TABLE_NAME + ", " + UserSongs.TABLE_NAME,  // Table to query
            projection,  // The array of columns to return
            selection,  // The columns for the WHERE clause
            selectionArgs,  // The values for the WHERE clause
            null,  // Don't group the rows
            null,  // Don't filter by the row groups
            sortOrder
        ).use { cursor ->                            // Order to sort
            while (cursor.moveToNext()) {
                val itemID: Long =
                    cursor.getLong(cursor.getColumnIndex(UserEntry.COLUMN_EMAIL_ADDRESS))
                itemsIds.add(itemID)
            }
        }
        // Greater than 0, the email exists - true.
        // Greater than 0, the email exists - true.
        return itemsIds.size > 0
    }


    // Navigation Bar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNav)
        val menu: Menu = bottomNavigation.menu
        val menuItem: MenuItem = menu.getItem(1)
        menuItem.isChecked = true


        removeSkips()
    }

    private val bottomNav = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navHome -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.navSong -> {
                val intent = Intent(this, SongsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navSettings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navShop -> {
                val intent = Intent(this, ShopActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    private fun playGenre(genre: String) {
        when (genre) {
            "Pop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:03Wky4J3HehgLEqgkYmia6")
            "Dance pop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DWZQaaqNMbbXa")
            "Rap" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:7920iKzK7vn64CPq1nKOot")
            "Pop rap" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:4OmZ3UxIO7MkvbB4OcTyYT")
            "Post-teen pop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:10FCW9lj0NdeoYI5VVvVtY")
            "Rock" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX1te6miphixI")
            "Latin" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:1g5CeeP9u4yz4GD5ET3JMf")
            "Hip hop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:2X5fbeF4DNx6xIQR1IPs2I")
            "Trap" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:70g9gDs4FE0kZPBC8ZTGGK")
            "Modern rock" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX1helbHcrYM1")
            "Electronic dance music" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:4SfxWYrRcYimPZvw8IxN0c")
            "Pop rock" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:4l7qxiQpxDOaby1aKlteQr")
            "Tropical house" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:0KcnVQPhrJT9rmqUJlfsKY")
            "Reggaeton" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:7xWuNevFBmwnFEg6wzdCc7")
            "Melodic rap" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:7ARx895Fpc4tIVqb10Q2tr")
            "Electropop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:2a5i2ZtEXGKwVGL16J8N0p")
            "Latin pop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:4THaOf47zyTm8I7QnqjbNm")
            "Classic rock" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:4lIywN6kXl9KPm3OQ8u8G7")
            "Soft rock" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:1Q0mFlwoI18Dc2jRuRXzQP")
            "Southern hip hop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:4lcyWQDOzPfcbZrcBI3FOW")
            "Post-grunge" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DWVwr24yj95lH")
            "Indie pop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DWWEcRhUVtL8n")
            "Alternative metal" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:5El2lUJbkMV257jKAWW7t3")
            "Metal" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:1jmtQDb0SQc6WVZx4p27qT")
            "Permanent wave" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:4EYSGTuqe9cVfSVpX4gtGv")
            "R&B" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:55HBRoDVGsGsnUTlCeKMyn")
            "Neo mellow" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:6ShJkjeYCGdg4wbniArbhA")
            "Contemporary country" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:49QHtigsIlJ7DhsBElxQjC")
            "Canadian pop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:5frCyNzCSe0JKtqaqHeG1A")
            "Electro house" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:7JO5s7xn8DfQpc3iRkUned")
            "Contemporary" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:1XbX5NYoSjKEFODoEeEMvV")
            "Alternative rock" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:4TxlNcEVMae5UsH8rlOwWR")
            "Hard rock" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:3szD2GmLTlyheotWjS0Ibw")
            "Folk rock" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:54yp3skLKD8ChD50lxVv4N")
            "Nu metal" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:6GEXSIPx7sWCKp6clPj1Ae")
            "K-pop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:0YceH1N4c5IaObYKfvglVK")
            "Country" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:1iMdFSK362wz1VmGqkYmrj")
            "Grupera" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:3F2RzWe1bkRs1O2ysMuzmN")
            "Trap Latino" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:7y50Cs9Ode7mbXalmZ7D8R")
            "Spanish rock" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:2Al10ckQ10esYZK1223D23")
            "Adult standards" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:0FmeDxPXfsozPAyWv9uFjL")
            "Urban" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:2UmMnCLrvZin13vLJ3RIQ3")
            "Alternative hip hop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DWTggY0yqBxES")
            "German hip hop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:4SG18Qgu0Ho57hL5KHqZos")
            "Indietronica" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX5ulsyQdTxii")
            "Gangster rap" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:2vtAeEflikzZh0aaSWpIYF")
            "Regional Mexican" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:3Zc5uMBcHIoU5BkH0ArzcM")
            "Big room" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:5Bx5niVgi3qGQQw06C0RKq")
            "Indie rock" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:1Z1ueuFAu0Ne64Dctpzmw6")
            "Latin hip hop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:3nH8aytdqNeRbcRCg3dw9q")
            "Underground hip hop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:2AbUmd0vlvyJO2Dp7HTb3d")
            "Art rock" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:2LV7KfJgKpCRmBN12hxo9e")
            "Banda" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:2K1RSUCgsLlB4wyWpUJC4q")
            "Euro pop" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:4vuWieduKii4KZrUboVr9d")
            "Dance rock" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:4dldaSIsYKcYkuJgLRMqiw")
            "Reggae" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:2HyT0EFU7k1uizIGwhAd1Q")
            "Country rock" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:7xFm2nNChDO4PbLxNeEv6v")
            "Soul" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:73sIU7MIIIrSh664eygyjm")
            "Blues" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:03Wky4J3HehgLEqgkYmia6")
            "Jazz" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:7uc02r8GhUCBegTUXgrYZ2")
            "Funk" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:1iyr0IvBiVMYnnDDxNTe9G")
            "Spanish dance" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:19WBofaCaSEeCRG4WM2HKV")
            "Film" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:5GhatXsZVNYxrhqEAfZPLR")
            "Video game" -> spotifyAppRemote!!.playerApi.play("spotify:playlist:7lv2JeRaHu2unEJgENSXoP")
        }

        Thread.sleep(200)

        // Get song data
        songInfo()
        //albumImage()

        // Stop
        playPause = true
        playButtonClick(playButton)


        val songText: TextView = findViewById(R.id.textSongName)
        songText.text = songName

        val artistText: TextView = findViewById(R.id.textAtristName)
        artistText.text = artistName
    }
}