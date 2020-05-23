package com.example.breakout

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Slide
import android.view.*
import android.widget.*
import com.example.account_res.InputValidation
import com.example.breakout.adapters.ShopAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import kotlin.collections.ArrayList

class ShopActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    //Pull from database
    private var totalCurrency: Int = 0

    private var price: Int = 0
    private var offers: ArrayList<ShopItem> ? = null

    private var popupView: View ? = null

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

        offers.add(ShopItem(R.drawable.unknown_image, "5"))
        offers.add(ShopItem(R.drawable.unknown_image, "10"))
        offers.add(ShopItem(R.drawable.unknown_image, "15"))
        offers.add(ShopItem(R.drawable.unknown_image, "20"))
        offers.add(ShopItem(R.drawable.unknown_image, "25"))
        offers.add(ShopItem(R.drawable.unknown_image, "40"))
        offers.add(ShopItem(R.drawable.unknown_image, "60"))
        offers.add(ShopItem(R.drawable.unknown_image, "80"))
        offers.add(ShopItem(R.drawable.unknown_image, "100"))
        offers.add(ShopItem(R.drawable.unknown_image, "120"))

        return offers
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val inflater = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        popupView = inflater.inflate(R.layout.popup_payment, null)

        val popupWidth = 1000
        val popupHeight = 600
        val popupWindow = PopupWindow(popupView, popupWidth, popupHeight, true)

        popupWindow.elevation = 10.0F

        // Create a new slide animation for popup window enter transition
        val slideIn = Slide()
        slideIn.slideEdge = Gravity.START
        popupWindow.enterTransition = slideIn

        // Slide animation for popup window exit transition
        val slideOut = Slide()
        slideOut.slideEdge = Gravity.END
        popupWindow.exitTransition = slideOut

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)


        var item: ShopItem = offers!![position]

        price = item.price!!.toInt()
//        Toast.makeText(applicationContext, item.price, Toast.LENGTH_SHORT).show()

        popupView?.findViewById<Button>(R.id.purchaseButton)?.setOnClickListener{

            val cardNumber = popupView?.findViewById<EditText>(R.id.cardNumber)?.text.toString()
            val expiryDate = popupView?.findViewById<EditText>(R.id.expiryDate)?.text.toString()
            val CVV = popupView?.findViewById<EditText>(R.id.CVV)?.text.toString()
            val currency = findViewById<TextView>(R.id.currency)

            if (!InputValidation.validateCard(cardNumber)) {
                Toast.makeText(this.application, "Invalid Card Number", Toast.LENGTH_SHORT).show()
            } else if (!InputValidation.validateCVV(CVV)) {
                Toast.makeText(this.application, "Invalid CVV", Toast.LENGTH_SHORT).show()
            } else if (!InputValidation.validateDate(expiryDate)) {
                Toast.makeText(this.application, "Invalid Date", Toast.LENGTH_SHORT).show()
            }
            else {
                totalCurrency = totalCurrency.plus(price)
//                println("Total price is $totalCurrency")
                Toast.makeText(this.application, "Purchase Successful", Toast.LENGTH_SHORT).show()
                currency.text = totalCurrency.toString()
                popupWindow.dismiss()
            }
        }
    }
}
