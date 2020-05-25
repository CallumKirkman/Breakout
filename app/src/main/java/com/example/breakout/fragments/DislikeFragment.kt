package com.example.breakout.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import com.example.breakout.R
import com.example.breakout.adapters.DislikeAdapter
import com.example.breakout.items.DislikeItem

class DislikeFragment : Fragment(), AdapterView.OnItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dislike, container, false)
    }

    private var disliked: ArrayList<DislikeItem>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gridView = view.findViewById<GridView>(R.id.dislikeGrid)
        disliked = ArrayList()
        disliked = setDataList()

        val adapter = DislikeAdapter(disliked!!)
        gridView.adapter = adapter

        gridView.onItemClickListener = this
    }

    private fun setDataList(): ArrayList<DislikeItem> {

        var disliked: ArrayList<DislikeItem> = ArrayList()

        disliked.add(DislikeItem(R.drawable.unknown_album_image, "1Song", "URI1"))
        disliked.add(DislikeItem(R.drawable.unknown_album_image, "2Song", "URI2"))
        disliked.add(DislikeItem(R.drawable.unknown_album_image, "3Song", "URI3"))
        disliked.add(DislikeItem(R.drawable.unknown_album_image, "4Song", "URI4"))
        disliked.add(DislikeItem(R.drawable.unknown_album_image, "5Song", "URI5"))

        return disliked
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("Not yet implemented")
    }
}