package com.example.breakout

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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import kotlinx.android.synthetic.main.activity_player.*


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
        // Play a track
        spotifyAppRemote!!.playerApi.play("spotify:album:3T4tUhGYeRNVUGevb0wThu")
        Thread.sleep(50)
        spotifyAppRemote!!.playerApi.pause()

        // User will be given a pre-selected (or random) song for music preference, in case new
        // If they look up a song, or have previous preference data those songs will be used


        songInfo("")
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


    // Song information
    private fun songInfo(info: String): String {
        spotifyAppRemote!!.playerApi.subscribeToPlayerState().setEventCallback {
            val track: Track = it.track
            //println("Track = $track")
            songName = track.name.toString()
            println("Song name = $songName")
            artistName = track.artist.name.toString()
            //println("Artist name = $artistName")
            albumCover = track.album.toString()
            //println("Album cover = $albumCover")
            length = track.duration.toString()
            //println("Length = $length")
        }

        return when (info) {
            "artist" -> artistName
            "album" -> albumCover
            "length" -> length
            else -> songName
        }
    }


    // Song control
    private var playPause = false

    fun playButtonClick(view: View) {

        if (playPause) {
            // Stop
            playPause = false
            playButton.setImageResource(R.drawable.ic_play_arrow)
            spotifyAppRemote!!.playerApi.pause()
        } else {
            // Start
            playPause = true
            playButton.setImageResource(R.drawable.ic_pause)
            //spotifyAppRemote!!.playerApi.play("spotify:album:3T4tUhGYeRNVUGevb0wThu")
            spotifyAppRemote!!.playerApi.resume()

            // Get song name
            songInfo("song")

            val songText: TextView = findViewById(R.id.textSongName)
            songText.text = songName

            // Get artist name
            songInfo("artist")

            val artistText: TextView = findViewById(R.id.textAtristName)
            artistText.text = artistName
        }
    }

    fun likeButtonClick(view: View) {
        val likeButton: ImageView = findViewById(R.id.likeButton)
        likeButton.setBackgroundResource(R.drawable.like_button_flash);
        val likeAnimation = likeButton.background as AnimationDrawable?
        likeAnimation?.start()
        // If not added?
        // Add to favourite songs
        //spotifyAppRemote!!.userApi.addToLibrary("Current Song")
    }

    fun dislikeButtonClick(view: View) {
        val dislikeButton: ImageView = findViewById(R.id.dislikeButton)
        dislikeButton.setBackgroundResource(R.drawable.dislike_button_flash);
        val dislikeAnimation = dislikeButton.background as AnimationDrawable?
        dislikeAnimation?.start()
        // Remove from play list?
        //spotifyAppRemote!!.userApi.removeFromLibrary("Current Song")
    }

    fun skipButtonClick(view: View) {
        // Skip song
        spotifyAppRemote!!.playerApi.skipNext()
    }

    fun previousButtonClick(view: View) {
        // Previous song
        spotifyAppRemote!!.playerApi.skipPrevious()
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
            R.id.navFavourites -> {
                val intent = Intent(this, LikeActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navSettings -> { // Settings/removed songs
                val intent = Intent(this, DislikeActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navShop -> { // Settings/removed songs
                val intent = Intent(this, ShopActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}