package com.example.breakout

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector.ConnectionListener
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity : AppCompatActivity() {

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
        // Play a playlist
        //spotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")
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


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.navHome -> {
                println("Home clicked")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navFavourites -> {
                println("Favourites clicked")
                return@OnNavigationItemSelectedListener true
            }
            R.id.navSettings -> {
                println("Settings clicked")
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    private var playPause = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        bottomNavigation.setOnNavigationItemReselectedListener { onNavigationItemSelectedListener }
    }


    fun playButtonClick(view: View) {

        if (playPause) {
            // Stop
            //mp.pause()
            playPause = false
            playButton.setBackgroundResource(R.drawable.play)
            spotifyAppRemote!!.playerApi.pause()
        } else {
            // Start
            //mp.start()
            playPause = true
            playButton.setBackgroundResource(R.drawable.stop)
            spotifyAppRemote!!.playerApi.resume()
        }
    }

    fun likeButtonClick(view: View) {
        // Add to favourite songs
        val intent = Intent(this, LikeActivity::class.java) //Starts Like activity

        startActivity(intent)
    }

    fun dislikeButtonClick(view: View) {
        // Remove from play list?
        val intent = Intent(this, DislikeActivity::class.java) //Starts Dislike activity

        startActivity(intent)
    }
}