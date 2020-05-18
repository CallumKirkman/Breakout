package com.example.breakout;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.account_res.*;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity {

    private SQLiteDatabase mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserDBHelper dbHelper = new UserDBHelper(this);
        mDatabase = dbHelper.getReadableDatabase();
        TextView createAccountLink = findViewById(R.id.createAccountLink);

        createAccountLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch CreateAccountActivity from text link.
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            }
        });
    }


    /**
     * The login button. Check user input, look for the account.
     * If the account exists, log the user in and display the song player.
     *
     * @param view - view.
     */
    public void onClickLogin(View view) {
        // Input
        EditText e = findViewById(R.id.enterEmail);
        EditText p = findViewById(R.id.enterPassword);

        // Check if the email entered is a registered account.
        if (checkLoginCreds(e.getText().toString(), p.getText().toString())) {
            startActivity(new Intent(this, PlayerActivity.class));
        }
        else {
            Toast.makeText(LoginActivity.this, "Account Not Found", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Check the user input to see if the account exists.
     *
     * @param userEmail - the entered email address.
     * @param password - the entered password.
     * @return - true if the account exists and the password is correct, false otherwise.
     */
    private boolean checkLoginCreds(String userEmail, String password) {

        // TODO: Check the password is correct against the stored password.

        String[] projectionEmail = {
                UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS,
                UserDBContract.UserEntry.COLUMN_PASSWORD };

        String selectionEmail = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS +
                " LIKE ? AND " + UserDBContract.UserEntry.COLUMN_PASSWORD + " LIKE ? ";

        String[] selectionArgsEmail = {userEmail, password};

        // Sorting the results.
        String sortOrder = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS + " DESC";

        List itemsIdsEmail = new ArrayList<>();

        try (Cursor cursorEmail = mDatabase.query(
                UserDBContract.UserEntry.TABLE_NAME,    // Table to query
                projectionEmail,                        // The array of columns to return
                selectionEmail,                         // The columns for the WHERE clause
                selectionArgsEmail,                     // The values for the WHERE clause
                null,                                   // Don't group the rows
                null,                                   // Don't filter by the row groups
                sortOrder)) {                           // Order to sort

            while (cursorEmail.moveToNext()) {
                long itemID = cursorEmail.getLong(cursorEmail.getColumnIndexOrThrow(UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS));
                itemsIdsEmail.add(itemID);
            }
        }
        catch (Error e) {
            // TODO: Something here.
            System.out.println(e);
        }

        // If the table is larger than 0 the username is used within the db else it doesn't exist.
        // So its a new user without an account or inputted incorrectly.
        return itemsIdsEmail.size() > 0;
    }

}
