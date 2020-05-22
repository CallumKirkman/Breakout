package com.example.breakout.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.breakout.fragments.DislikeFragment
import com.example.breakout.fragments.FavouriteFragment
import com.example.breakout.fragments.SongFragment

class PageAdapter (private val context: Context, fm: FragmentManager, private var totalTabs: Int) : FragmentPagerAdapter(fm) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                SongFragment()
            }
            1 -> {
                FavouriteFragment()
            }
            else -> {
                DislikeFragment()
            }
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}