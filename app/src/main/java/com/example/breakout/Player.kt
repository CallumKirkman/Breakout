package com.example.breakout

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Player : AppCompatActivity() {

    private lateinit var mp: MediaPlayer
    private var totalTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        mp = MediaPlayer.create(this, R.raw.temperature)
        mp.isLooping = true
        mp.setVolume(0.5F, 0.5F)
        totalTime = mp.duration
    }

    fun playButtonClick(view: View) {

        if (mp.isPlaying) {
            //Stop
            mp.pause()
            //Make play button
        } else {
            //Start
            mp.start()
            //Make pause
        }
    }





}
