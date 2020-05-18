package com.example.breakout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SongsAdapter(private val songs: List<String>) : RecyclerView.Adapter<SongsAdapter.ViewHolder>() {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val songText = itemView.findViewById<TextView>(R.id.songName)
        //val songImage = itemView.findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val songView = inflater.inflate(R.layout., parent, false)
        // Return a new holder instance
        return ViewHolder(songView)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongsAdapter.ViewHolder, position: Int) {
        // Get the data model based on position
        val song: Songs = songs[position]
        // Set item views based on your views and data model
        val textView = holder.songName
        textView.setText(song.name)
        //val songImage = holder.songImage
        //songImage.text = if (contact.isOnline) "Message" else "Offline"
        //songImage.isEnabled = contact.isOnline
    }
}