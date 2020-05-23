package com.example.breakout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class SettingsActivity : AppCompatActivity() {

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


    fun onAccountClick(view: View) {
        val intent = Intent(this, AccountActivity::class.java)

        startActivity(intent)
    }

    fun onTermsClick(view: View) {
        val inflater = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        val popupView = inflater.inflate(R.layout.popup_terms, null)


        val popupWidth = 700
        val popupHeight = 900
        val popupWindow = PopupWindow(popupView, popupWidth, popupHeight, true)

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    fun onLogoutClick(view: View) {
        // ToDo(Log out THEN)

        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent)
    }

    fun onDeleteClick(view: View) {
        // ToDo(Delete account)
    }
}
