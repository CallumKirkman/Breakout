package com.example.breakout

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.breakout.adapters.SettingsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class SettingsActivity : AppCompatActivity(), SettingsAdapter.OnSettingClickListener {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.settings_top_menu, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if(item.itemId == R.id.versionHistory) {
            startActivity(Intent(this, ChangelogActivity::class.java))
        }
        return true
    }


    // Navigation Bar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Objects.requireNonNull(supportActionBar)?.title = "Settings"

        setContentView(R.layout.activity_settings)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener(bottomNav)
        val menu: Menu = bottomNavigation.menu
        val menuItem: MenuItem = menu.getItem(0)
        menuItem.isChecked = true


        val settingList = findViewById<RecyclerView>(R.id.settingsList)

        val settings = listOf("Name", "Second name", "Email", "Password", "Logout")

        val adapter = SettingsAdapter(settings, this)
        settingList.adapter = adapter

        settingList.layoutManager = LinearLayoutManager(this)
        settingList.itemAnimator = DefaultItemAnimator()
    }

    private val bottomNav = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navSettings -> {
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
            R.id.navShop -> {
                val intent = Intent(this, ShopActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onSettingClick(setting: String, position: Int) {
        super.onSettingClick(setting, position)

        Toast.makeText(applicationContext, setting, Toast.LENGTH_SHORT).show()
    }
}
