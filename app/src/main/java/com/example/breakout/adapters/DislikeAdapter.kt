package com.example.breakout.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.breakout.R
import com.example.breakout.items.DislikeItem

class DislikeAdapter(var songs: ArrayList<DislikeItem>) : BaseAdapter() {

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
        var view: View = View.inflate(parent?.context, R.layout.item_dis, null)
        var album: ImageView = view.findViewById(R.id.removeThis)
        var name: TextView = view.findViewById(R.id.song)

        var listItem: DislikeItem = songs[position]
        album.setImageResource(listItem.image!!)
        name.text = listItem.name!!

        return view
    }
}