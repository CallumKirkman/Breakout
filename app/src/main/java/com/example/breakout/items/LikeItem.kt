package com.example.breakout.items

class LikeItem {

    var image: Int ? = 0
    var name: String ? = null
    var songID: String ? = null

    constructor(image: Int?, name: String?, songID: String?) {
        this.image = image
        this.name = name
        this.songID = songID
    }
}