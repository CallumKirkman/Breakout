package com.example.breakout

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

class DisSongs: AppCompatActivity() {
    companion object {
        var globalDislikeSongName: MutableList<String> = mutableListOf()
        var globalDislikeSongURI : MutableList<String> = mutableListOf()
        var globalDislikeImageURI: MutableList<Uri> = mutableListOf()
    }
}