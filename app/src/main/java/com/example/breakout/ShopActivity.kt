package com.example.breakout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import com.example.breakout.adapters.ShopAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import kotlin.collections.ArrayList

class ShopActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var offers: ArrayList<ShopItem> ? = null

    // Navigation Bar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Objects.requireNonNull(supportActionBar)?.title = "Shop"

        setContentView(R.layout.activity_shop)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNav)
        val menu: Menu = bottomNavigation.menu
        val menuItem: MenuItem = menu.getItem(3)
        menuItem.isChecked = true


        val gridView = findViewById<GridView>(R.id.shopGrid)
        offers = ArrayList()
        offers = setDataList()

        val adapter = ShopAdapter(applicationContext, offers!!)
        gridView.adapter = adapter

        gridView.onItemClickListener = this
    }

    private val bottomNav = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navShop -> {
                return@OnNavigationItemSelectedListener false
            }
            R.id.navHome -> {
                val intent = Intent(this, PlayerActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navSong -> {
                val intent = Intent(this, SongsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navSettings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun setDataList(): ArrayList<ShopItem> {

        var offers: ArrayList<ShopItem> = ArrayList()

        offers.add(ShopItem(R.drawable.unknown_image, "1"))
        offers.add(ShopItem(R.drawable.unknown_image, "2"))
        offers.add(ShopItem(R.drawable.unknown_image, "3"))
        offers.add(ShopItem(R.drawable.unknown_image, "4"))
        offers.add(ShopItem(R.drawable.unknown_image, "5"))
        offers.add(ShopItem(R.drawable.unknown_image, "6"))
        offers.add(ShopItem(R.drawable.unknown_image, "7"))
        offers.add(ShopItem(R.drawable.unknown_image, "8"))
        offers.add(ShopItem(R.drawable.unknown_image, "9"))
        offers.add(ShopItem(R.drawable.unknown_image, "10"))
        offers.add(ShopItem(R.drawable.unknown_image, "11"))
        offers.add(ShopItem(R.drawable.unknown_image, "12"))

        return offers
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        //Buy it
        var item: ShopItem = offers!![position]
        Toast.makeText(applicationContext, item.name, Toast.LENGTH_SHORT).show()
    }
}
