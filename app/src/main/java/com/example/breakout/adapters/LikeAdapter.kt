package com.example.breakout.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.example.breakout.PlayerActivity
import com.example.breakout.R
import com.example.breakout.items.LikeItem
import com.example.breakout.items.ShopItem
import com.spotify.protocol.types.Image
import kotlinx.android.synthetic.main.activity_player.*

class LikeAdapter(var songs: ArrayList<LikeItem>) : BaseAdapter() {

    override fun getItem(position: Int): Any {
        return songs[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return songs.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        //Add other view inflate?
        var view: View = View.inflate(parent?.context, R.layout.item_fav, null)
        //var album: ImageView = view.findViewById(R.id.albumImage)
        var name: TextView = view.findViewById(R.id.song)

        var listItem: LikeItem = songs[position]
        //album.setImageURI(listItem.image!!)
        name.text = listItem.name!!

        return view
    }
}