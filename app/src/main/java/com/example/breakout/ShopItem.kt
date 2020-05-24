package com.example.breakout

class ShopItem {

    var offer: Int ? = 0
    var number: String ? = null
    var price: String ? = null

    constructor(offer: Int?, number: String?, price: String?) {
        this.offer = offer
        this.number = number
        this.price = price
    }
}