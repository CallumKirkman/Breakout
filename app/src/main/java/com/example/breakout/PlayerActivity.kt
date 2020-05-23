package com.example.breakout

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.breakout.fragments.GENRE
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.ImageUri
import com.spotify.protocol.types.Track
import kotlinx.android.synthetic.main.activity_player.*
import com.example.breakout.SongDBHelper


class PlayerActivity : AppCompatActivity() {

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
            println("Getting $imageUri")
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
        // Skip song
        spotifyAppRemote!!.playerApi.skipNext()

        // Get song data
        songInfo()
        albumImage()

        val songText: TextView = findViewById(R.id.textSongName)
        songText.text = songName

        val artistText: TextView = findViewById(R.id.textAtristName)
        artistText.text = artistName
    }

    fun previousButtonClick(view: View) {
        // Previous song
        spotifyAppRemote!!.playerApi.skipPrevious()

        // Get song data
        songInfo()
        albumImage()

        val songText: TextView = findViewById(R.id.textSongName)
        songText.text = songName

        val artistText: TextView = findViewById(R.id.textAtristName)
        artistText.text = artistName
    }

    private val dbHelper = UserDBHelper(this)

    private val mDatabase = dbHelper.writableDatabase

    fun likeButtonClick(view: View) {
        val likeButton: ImageView = findViewById(R.id.likeButton)
        likeButton.setBackgroundResource(R.drawable.like_button_flash);
        val likeAnimation = likeButton.background as AnimationDrawable?
        likeAnimation?.start()
        // If not added?
        // Add to favourite songs
        //spotifyAppRemote!!.userApi.addToLibrary(trackLink)



        val cV = ContentValues()
        cV.put(UserDBContract.SongStorage.COLUMN_SONG_NAME, songName)
        cV.put(UserDBContract.SongStorage.COLUMN_SONG_LIKE, 1)
        cV.put(UserDBContract.SongStorage.COLUMN_ARTIST_NAME, artistName)
        cV.put(UserDBContract.SongStorage.COLUMN_SONG_URI, trackLink)
        cV.put(UserDBContract.SongStorage.COLUMN_IMAGE_URI, imageUri.raw)


        mDatabase.insert(UserDBContract.SongStorage.TABLE_NAME, null, cV)


    }

    fun dislikeButtonClick(view: View) {
        val dislikeButton: ImageView = findViewById(R.id.dislikeButton)
        dislikeButton.setBackgroundResource(R.drawable.dislike_button_flash);
        val dislikeAnimation = dislikeButton.background as AnimationDrawable?
        dislikeAnimation?.start()
        // Remove from play list?
        //spotifyAppRemote!!.userApi.removeFromLibrary(trackLink)

        val cV = ContentValues()
        cV.put(UserDBContract.SongStorage.COLUMN_SONG_NAME, songName)
        cV.put(UserDBContract.SongStorage.COLUMN_SONG_LIKE, 1)
        cV.put(UserDBContract.SongStorage.COLUMN_ARTIST_NAME, artistName)
        cV.put(UserDBContract.SongStorage.COLUMN_SONG_URI, trackLink)
        cV.put(UserDBContract.SongStorage.COLUMN_IMAGE_URI, imageUri.raw)


        mDatabase.insert(UserDBContract.SongStorage.TABLE_NAME, null, cV)


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
        println("Genre selected")
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

        // Get song data
        songInfo()
        //albumImage()

        playPause = true
        playButton.setBackgroundResource(R.drawable.pause)

        val songText: TextView = findViewById(R.id.textSongName)
        songText.text = songName

        val artistText: TextView = findViewById(R.id.textAtristName)
        artistText.text = artistName
    }
}