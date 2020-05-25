package com.example.breakout.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.breakout.PlayerActivity
import com.example.breakout.R
import com.example.breakout.adapters.SongsAdapter

const val GENRE = "com.example.breakout.fragments.GENRE"

class SongFragment : Fragment(), SongsAdapter.OnSongClickListener{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = view.findViewById<SearchView>(R.id.searchBar)
        val genreList = view.findViewById<RecyclerView>(R.id.songList)

        val genres = listOf("Pop", "Dance pop", "Rap", "Pop rap", "Post-teen pop", "Rock", "Latin",
            "Hip hop", "Trap", "Modern rock", "Electronic dance music", "Pop rock", "Tropical house",
            "Reggaeton", "Melodic rap", "Electropop", "Latin pop", "Classic rock", "Soft rock",
            "Southern hip hop", "Post-grunge", "Indie pop", "Alternative metal", "Metal",
            "Permanent wave", "R&B", "Neo mellow", "Contemporary country", "Canadian pop",
            "Electro house", "Contemporary", "Alternative rock", "Hard rock",
            "Folk rock", "Nu metal", "K-pop", "Country", "Grupera", "Trap Latino",
            "Spanish rock", "Adult standards", "Urban", "Alternative hip hop",
            "German hip hop", "Indietronica", "Gangster rap", "Regional Mexican", "Big room",
            "Indie rock", "Latin hip hop", "Underground hip hop", "Art rock", "Banda", "Euro pop",
            "Dance rock", "Reggae", "Country rock", "Soul", "Blues", "Jazz",
            "Funk", "Spanish dance", "Film", "Video game")

        val adapter = SongsAdapter(genres, this)
        genreList.adapter = adapter

        genreList.layoutManager = LinearLayoutManager(context)
        genreList.itemAnimator = DefaultItemAnimator()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Give to player
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter(newText)
                return false
            }
        })
    }

    override fun onSongClick(songs: String, position: Int) {
        super.onSongClick(songs, position)
        //Intent to player
        val intent = Intent(context, PlayerActivity::class.java).apply {
            putExtra(GENRE, songs)
        }

        context?.startActivity(intent)
    }
}
