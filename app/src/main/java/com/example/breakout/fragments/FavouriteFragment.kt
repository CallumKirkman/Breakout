package com.example.breakout.fragments

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import com.example.breakout.FavSongs.Companion.globalImageURI
import com.example.breakout.FavSongs.Companion.globalSongName
import com.example.breakout.FavSongs.Companion.globalSongURI
import com.example.breakout.PlayerActivity
import com.example.breakout.R
import com.example.breakout.adapters.LikeAdapter
import com.example.breakout.items.LikeItem
import com.spotify.protocol.types.Image
import com.spotify.protocol.types.ImageUri

class FavouriteFragment : Fragment(), AdapterView.OnItemClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

//    var songName: MutableList<String> = mutableListOf()
//    var songURI: MutableList<String> = mutableListOf()
//    var imageURI: MutableList<Uri> = mutableListOf()

    private var favourites: ArrayList<LikeItem> ? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        val gridView = view.findViewById<GridView>(R.id.favGrid)
        favourites = ArrayList()
        favourites = setDataList()

        val adapter = LikeAdapter(favourites!!)
        gridView.adapter = adapter

        gridView.onItemClickListener = this

        println("Song name $globalSongName")
        println("Image URI $globalSongURI")
        println("Song URI $globalImageURI")
        println("Song URI $globalImageURI")
    }

    private fun setDataList(): ArrayList<LikeItem> {

        var liked: ArrayList<LikeItem> = ArrayList()

        var x = 0
        for (i in globalSongName) {
            liked.add(LikeItem(globalImageURI[x], globalSongName[x], globalSongURI[x]))
            x++
        }


//        offers.add(LikeItem(R.drawable.unknown_album_image, "Song2", "URI2"))
//        offers.add(LikeItem(R.drawable.unknown_album_image, "Song3", "URI3"))
//        offers.add(LikeItem(R.drawable.unknown_album_image, "Song4", "URI4"))
//        offers.add(LikeItem(R.drawable.unknown_album_image, "Song5", "URI5"))

        return liked
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }
}