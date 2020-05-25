package com.example.breakout.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import com.example.breakout.R
import com.example.breakout.adapters.LikeAdapter
import com.example.breakout.items.LikeItem

class FavouriteFragment : Fragment(), AdapterView.OnItemClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    private var favourites: ArrayList<LikeItem> ? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gridView = view.findViewById<GridView>(R.id.favGrid)
        favourites = ArrayList()
        favourites = setDataList()

        val adapter = LikeAdapter(favourites!!)
        gridView.adapter = adapter

        gridView.onItemClickListener = this
    }

    private fun setDataList(): ArrayList<LikeItem> {

        var offers: ArrayList<LikeItem> = ArrayList()


        offers.add(LikeItem(R.drawable.unknown_album_image, "Song1", "1"))

//        offers.add(LikeItem(R.drawable.unknown_album_image, "Song2", "2"))
//        offers.add(LikeItem(R.drawable.unknown_album_image, "Song3", "3"))
//        offers.add(LikeItem(R.drawable.unknown_album_image, "Song4", "4"))
//        offers.add(LikeItem(R.drawable.unknown_album_image, "Song5", "5"))

        return offers
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }
}
