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

    private var mSpotifyAppRemote: SpotifyAppRemote? = null

    override fun onStart() {
        super.onStart()
        val connectionParams =
            ConnectionParams.Builder(CLIENT_ID)
                .setRedirectUri(REDIRECT_URI)
                .showAuthView(true).build()
        SpotifyAppRemote.connect(this, connectionParams,
            object : ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    mSpotifyAppRemote = spotifyAppRemote
                    Log.d("MainActivity", "Connected! Yay!")
                    connected()
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MainActivity", throwable.message, throwable)
                }
            })
    }

    private fun connected() {
        // Play a playlist
        mSpotifyAppRemote!!.playerApi.play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL")

        //Spotify song request - uses 62 base ID number at end of URI ^

        //Either: request based if Intent from Player
        //Or merge two files. Pretty sure I can
        //Avoids calling and inheritance on each new call

        //Most likely not needed in any other file?
    }

    override fun onStop() {
        super.onStop()
    }

    companion object {
        private const val CLIENT_ID = "daa95815630947bd980906b32437654d"
        private const val REDIRECT_URI = "com.example.breakout:/callback"
    }



    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        mp = MediaPlayer.create(this, R.raw.temperature)
        mp.isLooping = true
        mp.setVolume(0.5F, 0.5F)
        totalTime = mp.duration


        //Position bar
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

                //Update position bar
                positionBar.progress = currentPosition

                //Update Labels
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

        //Thread
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

        if (mp.isPlaying) {
            //Stop
            mp.pause()
            playButton.setBackgroundResource(R.drawable.play)
        } else {
            //Start
            mp.start()
            playButton.setBackgroundResource(R.drawable.stop)
        }
    }
}