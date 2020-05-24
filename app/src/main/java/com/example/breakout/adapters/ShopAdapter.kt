package com.example.breakout.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.breakout.R
import com.example.breakout.ShopItem

class ShopAdapter(var context: Context, var offers: ArrayList<ShopItem>) : BaseAdapter() {

    override fun getItem(position: Int): Any {
        return offers[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return offers.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        //Add other view inflate?
        var view: View = View.inflate(context, R.layout.item_shop, null)
        var icon: ImageView = view.findViewById(R.id.offer)
        var number: TextView = view.findViewById(R.id.vinyls)
        var price: TextView = view.findViewById(R.id.price)

        var listItem: ShopItem = offers[position]
        icon.setImageResource(listItem.offer!!)
        number.text = listItem.number!!
        price.text = listItem.price!!

        return view
    }

}