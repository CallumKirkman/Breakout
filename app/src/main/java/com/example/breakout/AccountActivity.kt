package com.example.breakout

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.account_res.InputValidation
import com.example.account_res.PasswordUtilities
import java.security.NoSuchAlgorithmException
import java.util.*

class AccountActivity : AppCompatActivity() {

    private val mDatabase: SQLiteDatabase? = null
    private val mReadDatabase: SQLiteDatabase? = null
    private val algorithm = "SHA-256"

    private fun checkUserEmailExists(userEmail: String): Boolean {

        val projection = arrayOf(UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS)
        val selection = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS + " LIKE ? "
        val selectionArgs = arrayOf(userEmail)

        // Sorting the results.
        val sortOrder = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS + " DESC"
        val itemsIds: MutableList<String> = mutableListOf()
        try {
            mReadDatabase!!.query(
                UserDBContract.UserEntry.TABLE_NAME,  // Table to query
                projection,  // The array of columns to return
                selection,  // The columns for the WHERE clause
                selectionArgs,  // The values for the WHERE clause
                null,  // Don't group the rows
                null,  // Don't filter by the row groups
                sortOrder).use { cursor -> // Order to sort
                while (cursor.moveToNext()) {
                    val itemID = cursor.getString(cursor.getColumnIndexOrThrow(UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS))
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

        assert(supportActionBar != null)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setContentView(R.layout.activity_account)

        // Fill all boxes with user details
    }

    fun onSubmitClick(view: View) {

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
                        this, "An error occurred.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    this, "Email is associated with another account", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
