package com.example.breakout;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
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
                startActivity(new Intent(getBaseContext(), CreateAccountActivity.class));
            }
        });
    }

    // Check if email used has an account
    public void onClickLogin(View view) {
        // Get email, password.
        EditText email = findViewById(R.id.enterEmail);
        EditText p = findViewById(R.id.enterPassword);

        if (checkLoginCreds(email.getText().toString(), p.getText().toString()) == true) {
            Intent login = new Intent(this, PlayerActivity.class);
            startActivity(login);
        } else {
            Toast.makeText(LoginActivity.this, "Enter Valid username", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkLoginCreds(String userEmail, String password) {

        String[] projectionEmail = {
                UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS,
                UserDBContract.UserEntry.COLUMN_PASSWORD
        };

//
        String selectionEmail = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS + " LIKE ? AND " + UserDBContract.UserEntry.COLUMN_PASSWORD + " LIKE ? ";
        String[] selectionArgsEmail = {userEmail, password};

        //sorting the results
        String sortOrder = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS + " DESC";

        List itemsIdsEmail = new ArrayList<>();
        Cursor cursorEmail = null;

        try {
            cursorEmail = mDatabase.query(
                    UserDBContract.UserEntry.TABLE_NAME,    // Table to query
                    projectionEmail,                             // The array of columns to return
                    selectionEmail,                              // The columns for the WHERE clause
                    selectionArgsEmail,                          // The values for the WHERE clause
                    null,                          // Don't group the rows
                    null,                           // Don't filter by teh row groups
                    sortOrder                              // Order to sort
            );



            while (cursorEmail.moveToNext()) {
                long itemID = cursorEmail.getLong(cursorEmail.getColumnIndexOrThrow(UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS));
                itemsIdsEmail.add(itemID);
            }




        } catch (Error e) {
            System.out.println(e);
        } finally {
            cursorEmail.close();
        }

        // If the table is larger than 0 the username is used within the db else it doesn't exist
        // so its a new user without an account or inputted incorrectly
        if (itemsIdsEmail.size() > 0 ) {
            return true;
        } else {
            return false;
        }
    }


}
