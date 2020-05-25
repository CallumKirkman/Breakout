package com.example.breakout.items

import com.spotify.protocol.types.ImageUri
import com.spotify.protocol.types.Uri

class LikeItem {

    var image: android.net.Uri ? = null
    var name: String ? = null
    var songURI: String ? = null

    constructor(image: android.net.Uri?, name: String?, songURI: String?) {
        this.image = image
        this.name = name
        this.songURI = songURI
    }
}