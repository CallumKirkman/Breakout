package com.example.breakout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_song.*


class SongFragment : Fragment() {

    lateinit var genreList : RecyclerView //.Adapter<RecyclerView.ViewHolder>
    var genre:MutableList<String> = ArrayList()
    var displayList:MutableList<String> = ArrayList()

    private val songList = listOf<String>("Pop", "Rock", "Indie", "K-pop", "Country", "Country rock", "Indie pop")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()

        val searchItem = searchBar.query as SearchView

        val adapter = SongsAdapter(songList)
        genreList.adapter = adapter
        genreList.layoutManager = LinearLayoutManager(context)


        //abstract class Adapter<VH : RecyclerView.ViewHolder>
        //genreList.adapter


        searchItem.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                println("Testing2 $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song, container, false)
    }

    private fun loadData() {
    }
}
