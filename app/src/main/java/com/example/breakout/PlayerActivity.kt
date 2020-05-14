package com.example.breakout

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
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
        //spotifyAppRemote!!.playerApi.play("spotify:track:2PpruBYCo4H7WOBJ7Q2EwM")

        // User will be given a pre-selected (or random) song for music preference, in case new
        // If they look up a song, or have previous preference data those songs will be used
    }

    override fun onStop() {
        super.onStop()
        // Handle errors
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }


    // Song information
    fun songInfo() {
        spotifyAppRemote!!.playerApi.subscribeToPlayerState().setEventCallback {
            val track: Track = it.track
            //println("Track = $track")
            val songName = track.name
            //println("Song name = $songName")
            val artistName = track.artist.name
            //println("Artist name = $artistName")
            val albumCover = track.album
            //println("Album cover = $albumCover")
            val uriForTrack = track.uri
            //println("Track URI = $uriForTrack")
            val length = track.duration
            //println("Length = $length")
            val image = track.imageUri
            //println("Image = $image")
        }
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
            playButton.setBackgroundResource(R.drawable.stop)
            spotifyAppRemote!!.playerApi.play("spotify:track:2PpruBYCo4H7WOBJ7Q2EwM")
            //spotifyAppRemote!!.playerApi.resume()
        }
    }

    fun likeButtonClick(view: View) {
        val likeButton: ImageView = findViewById(R.id.likeButton)
        likeButton.setBackgroundResource(R.drawable.like_button_flash);
        val likeAnimation = likeButton.background as AnimationDrawable?
        likeAnimation?.start()
        // If not added?
        // Add to favourite songs
    }

    fun dislikeButtonClick(view: View) {
        val dislikeButton: ImageView = findViewById(R.id.dislikeButton)
        dislikeButton.setBackgroundResource(R.drawable.dislike_button_flash);
        val dislikeAnimation = dislikeButton.background as AnimationDrawable?
        dislikeAnimation?.start()
        // Remove from play list?
    }
}