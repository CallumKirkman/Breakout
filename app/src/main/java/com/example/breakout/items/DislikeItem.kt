package com.example.breakout.items

class DislikeItem {

    var image: Int ? = 0
    var name: String ? = null
    var songURI: String ? = null

    constructor(image: Int?, name: String?, songURI: String?) {
        this.image = image
        this.name = name
        this.songURI = songURI
    }
}