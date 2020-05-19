package com.example.breakout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SongFragment : Fragment(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = view.findViewById<SearchView>(R.id.searchBar)
        val genreList = view.findViewById<RecyclerView>(R.id.songList)

        val genres = listOf("Pop", "Rock", "Indie", "Country", "Jazz", "Indie Rock","K-pop",
            "Classical", "Classical Rock", "Pop Rock", "Indie pop", "Smooth Jazz", "Punk Rock")

        val adapter = SongsAdapter(genres)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song, container, false)
    }
}
