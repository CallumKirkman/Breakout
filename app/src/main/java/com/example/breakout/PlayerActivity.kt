package com.example.breakout

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
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
        spotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")
        // User will be given a pre-selected (or random) song for music preference, in case new
        // If they look up a song, or have previous preference data those songs will be used

//        // Subscribe to PlayerState
//        spotifyAppRemote!!.playerApi.playerState
//            .setResultCallback {
//                Log.d("PlayerActivity", "Connected")
//            }
//            .setErrorCallback {
//                Log.e("PlayerActivity", "Fail")
//            }


//        // Subscribe to PlayerState
//        spotifyAppRemote!!.playerApi.subscribeToPlayerState().setEventCallback {
//            val track: Track = it.track
//            Log.d("MainActivity", track.name + " by " + track.artist.name)
//        }
    }

    override fun onStop() {
        super.onStop()
        // Handle errors
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }


    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0
    private var playPause = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        mp = MediaPlayer.create(this, R.raw.temperature)
        mp.isLooping = true
        mp.setVolume(0.5F, 0.5F)
        totalTime = mp.duration


        // Position bar
        positionBar.max = totalTime
        positionBar.setOnSeekBarChangeListener(
            object  : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        mp.seekTo(progress)
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }
                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }

            }
        )

        @SuppressLint("HandlerLeak")
        var handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                var currentPosition = msg.what

                // Update position bar
                positionBar.progress = currentPosition

                // Update Labels
                var elapsedTime = createTimeLabel(currentPosition)
                elapsedTimeLabel.text = elapsedTime

                var remainingTime = createTimeLabel(totalTime - currentPosition)
                remainingTimeLabel.text = "-$remainingTime"
            }

            private fun createTimeLabel(time: Int): String {
                var timeLabel = ""
                var min = time / 1000 / 60
                var sec = time / 1000 % 60

                timeLabel = "$min"
                if (sec < 10) timeLabel += "0"
                timeLabel += sec

                return timeLabel
            }
        }

        // Thread
        Thread(Runnable {
            while (mp != null) {
                try {
                    var message = Message()
                    message.what = mp.currentPosition
                    handler.sendMessage(message)
                    Thread.sleep(1000)
                } catch (e:InterruptedException) {}
            }
        }).start()
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
}