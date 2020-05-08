package com.example.breakout

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_player.*

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
