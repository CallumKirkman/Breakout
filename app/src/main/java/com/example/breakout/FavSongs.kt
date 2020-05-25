package com.example.breakout

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

class FavSongs: AppCompatActivity() {
    companion object {
        var globalSongName: MutableList<String> = mutableListOf()
        var globalSongURI : MutableList<String> = mutableListOf()
        var globalImageURI: MutableList<Uri> = mutableListOf()
    }
}