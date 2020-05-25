package com.example.breakout

import android.accounts.Account
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.account_res.InputValidation
import com.example.account_res.PasswordUtilities
import com.example.breakout.UserDBContract.CurrentUser
import com.example.breakout.UserDBContract.UserEntry
import kotlinx.android.synthetic.main.activity_account.*
import java.security.NoSuchAlgorithmException
import java.util.*

class AccountActivity : AppCompatActivity() {

    private var popupView: View? = null

    private val algorithm = "SHA-256"

    private fun checkUserEmailExists(userEmail: String): Boolean {
        val dbHelper = UserDBHelper(this)
        val mDatabase = dbHelper.readableDatabase

        val projection = arrayOf(UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS)
        val selection = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS + " LIKE ? "
        val selectionArgs = arrayOf(userEmail)

        // Sorting the results.
        val sortOrder = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS + " DESC"
        val itemsIds: MutableList<String> = mutableListOf()
        try {
            mDatabase!!.query(
                UserDBContract.UserEntry.TABLE_NAME,  // Table to query
                projection,  // The array of columns to return
                selection,  // The columns for the WHERE clause
                selectionArgs,  // The values for the WHERE clause
                null,  // Don't group the rows
                null,  // Don't filter by the row groups
                sortOrder
            ).use { cursor -> // Order to sort
                while (cursor.moveToNext()) {
                    val itemID =
                        cursor.getString(cursor.getColumnIndexOrThrow(UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS))
                    itemsIds.add(itemID)
                }
            }
        } catch (e: Error) {
            println(e)
        }

        // If the table is larger than 0 the username is used within the db else it doesn't exist.
        // So its a new user without an account or inputted incorrectly.
        return itemsIds.size > 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.actionBar?.setHomeButtonEnabled(true)
        Objects.requireNonNull(supportActionBar)?.title = "Account"



        getUserInfo()




        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_account)

        //call method which gets all user current user data and puts it within the boxes


        // ToDo(Fill all boxes with user details)
    }

    fun onSubmitClick(view: View) {

        val dbHelper = UserDBHelper(this)
        val mDatabase = dbHelper.readableDatabase

        val forename = findViewById<EditText>(R.id.nameText).text.toString()
        val surname = findViewById<EditText>(R.id.secondNameText).text.toString()
        val password = findViewById<EditText>(R.id.passwordText).text.toString()
        val confirm = findViewById<EditText>(R.id.confirmText).text.toString()
        val email = findViewById<EditText>(R.id.emailText).text.toString()

        if (!InputValidation.validateEmail(email)) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show()
        } else if (!InputValidation.validatePassword(password)) {
            Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show()
        } else if (!InputValidation.validateName(forename) && !InputValidation.validateName(surname)) {
            Toast.makeText(this, "Invalid Name", Toast.LENGTH_SHORT).show()
        } else if (password != confirm) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
        }
        // Salt and hash the password. Store the account.
        else {
            if (!checkUserEmailExists(email)) {
                try {
                    val salt = PasswordUtilities.generateSalt(4)
                    val hashBytes =
                        PasswordUtilities.generateHash(password, salt, algorithm)
                    val hash = PasswordUtilities.hexBytes(hashBytes)
                    val cV = ContentValues()
                    cV.put(UserDBContract.UserEntry.COLUMN_FORENAME, forename)
                    cV.put(UserDBContract.UserEntry.COLUMN_SURNAME, surname)
                    cV.put(UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS, email)
                    cV.put(UserDBContract.UserEntry.COLUMN_PASSWORD, hash)
                    cV.put(UserDBContract.UserEntry.COLUMN_SALT, salt)
                    mDatabase?.insert(UserDBContract.UserEntry.TABLE_NAME, null, cV)
                    startActivity(Intent(this, PlayerActivity::class.java))
                } catch (exc: NoSuchAlgorithmException) {
                    Toast.makeText(
                        this, "An error occurred.", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this, "Email is associated with another account", Toast.LENGTH_SHORT
                ).show()
            }
            // ToDo(Change account details)
            updateUserData(nameText.toString(),secondNameText.toString(), emailText.toString(), passwordText.toString())
            Toast.makeText(this, "Successful change", Toast.LENGTH_SHORT).show()

            Thread.sleep(500)
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    fun onDeleteClick(view: View) {

        val inflater = (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        popupView = inflater.inflate(R.layout.popup_delete, null)

        val popupWidth = LinearLayout.LayoutParams.MATCH_PARENT
        val popupHeight = 600
        val popupWindow = PopupWindow(popupView, popupWidth, popupHeight, true)

        popupWindow.elevation = 10.0F

        // Create a new slide animation for popup window enter transition
        val slideIn = Slide()
        slideIn.slideEdge = Gravity.TOP
        popupWindow.enterTransition = slideIn

        // Slide animation for popup window exit transition
        val slideOut = Slide()
        slideOut.slideEdge = Gravity.BOTTOM
        popupWindow.exitTransition = slideOut

        popupWindow.showAtLocation(view, Gravity.TOP, 0, 0)

        popupView?.findViewById<Button>(R.id.deleteButton)?.setOnClickListener {

            val email = popupView?.findViewById<EditText>(R.id.email)?.text.toString()
            val password = popupView?.findViewById<EditText>(R.id.password)?.text.toString()

            // ToDo(Make sure correct email and password)
//            if (!InputValidation.validateCard(email)) {
//                Toast.makeText(this.application, "Email Incorrect", Toast.LENGTH_SHORT).show()
//            } else if (!InputValidation.validateCVV(password)) {
//                Toast.makeText(this.application, "Password Incorrect", Toast.LENGTH_SHORT).show()
//            }
//            else {
            // ToDo(Delete account)
            deleteUser()
            Toast.makeText(this.application, "Delete Successful", Toast.LENGTH_SHORT).show()
            popupWindow.dismiss()

            Thread.sleep(2500) //ToDo(Is this needed? Makes it seem like a real database though)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

//            }
        }
    }

    private fun getUserInfo() {
        //get current user
        //get all their account info
        //  -going to need to revert hash(already done within loginActivity)
        //  -when updating info going to have to use the same hash function (use same or new?)

        //get user to enter password
        //if first password == normal hashed one take confirm password as the new password then update off confirm

        val dbHelper = UserDBHelper(this)
        val mDatabase = dbHelper.readableDatabase

        val projection = arrayOf(
            UserEntry.COLUMN_EMAIL_ADDRESS, UserEntry.COLUMN_FORENAME, UserEntry.COLUMN_SURNAME,
            UserEntry.COLUMN_PASSWORD, UserEntry.COLUMN_SALT
        )

        //stored variables
        var forename = ""
        var surname = ""
        var email = ""
        var password = ""
        var salt = ""

        try {

            val query =
                "Select USER_FORENAME, USER_SURNAME,USER_EMAIL_ADDRESS, USER_PASSWORD, USER_SALT FROM TBL_USERDATA, TBL_CURRENT WHERE TBL_USERDATA.USER_EMAIL_ADDRESS = TBL_CURRENT.CURRENT_USER_EMAIL"
            val cursor = mDatabase.rawQuery(query, null)

            //should only iterate once, only the current active user should be able to do this
            while (cursor.moveToNext()) {
                forename = cursor.getString(cursor.getColumnIndexOrThrow(UserEntry.COLUMN_FORENAME))
                surname = cursor.getString(cursor.getColumnIndexOrThrow(UserEntry.COLUMN_SURNAME))
                email =
                    cursor.getString(cursor.getColumnIndexOrThrow(UserEntry.COLUMN_EMAIL_ADDRESS))
                password = cursor.getString(cursor.getColumnIndexOrThrow(UserEntry.COLUMN_PASSWORD))
                salt = cursor.getString(cursor.getColumnIndexOrThrow(UserEntry.COLUMN_SALT))
            }

            fillAccountBoxes(forename, surname, email)


            //convert password back to plaintext
            val byteHash = PasswordUtilities.generateHash(password, salt, "SHA-256")
            val hash = PasswordUtilities.hexBytes(byteHash)
            if (hash.equals(password)) {

            }

        }
        finally {

        }

    }

    private fun fillAccountBoxes(forename: String, surname: String, email: String) {

        //val nameText = findViewById(R.id.nameText) as EditText
        nameText.setText(forename).toString()
        secondNameText.setText(surname).toString()
        emailText.setText(email).toString()


    }

    private fun updateUserData(forename:String, surname: String, email: String, password:String)
    {






    }

    fun deleteUser() {

    }

}
