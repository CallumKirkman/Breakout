package com.example.breakout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SongsAdapter(private val songs: List<String>, var clickListener: OnSongClickListener) : RecyclerView.Adapter<SongsAdapter.ViewHolder>() {

    var filteredSongs = songs

    interface OnSongClickListener {
        fun onSongClick(songs: String, position: Int) {
        }
    }


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val songText: TextView = itemView.findViewById(R.id.songName)

        fun clickAction(songs: String, action: OnSongClickListener) {
            songText.text = songs

            itemView.setOnClickListener {
                action.onSongClick(songs, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val songView = inflater.inflate(R.layout.item_song, parent, false)
        // Return a new holder instance
        return ViewHolder(songView)
    }

    override fun getItemCount(): Int {
        return filteredSongs.size
    }

    override fun onBindViewHolder(holder: SongsAdapter.ViewHolder, position: Int) {

        holder.clickAction(filteredSongs[position], clickListener)
    }

    fun filter(newText: String) {
        val lowerText = newText.toLowerCase(Locale.ENGLISH)

        val newSongList = songs.filter { lowerText in it.toLowerCase(Locale.ENGLISH) }
        filteredSongs = newSongList
        notifyDataSetChanged()
    }
}

