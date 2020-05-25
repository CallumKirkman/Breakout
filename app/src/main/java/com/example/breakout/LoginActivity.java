package com.example.breakout;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import com.example.account_res.*;
import com.example.breakout.UserDBContract.CurrentUser;
import com.example.breakout.UserDBContract.UserEntry;

public class LoginActivity extends Activity {

    private static SQLiteDatabase mDatabase;
    private static Object PlayerActivity;
    private UserDBHelper dbHelper = new UserDBHelper(this);


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
        EditText e = findViewById(R.id.enterEmail);
        EditText p = findViewById(R.id.enterPassword);

        // Check if the email entered is a registered account and the password is correct.
        if (checkLoginCreds(e.getText().toString(), p.getText().toString())) {
            //UPDATE CURRENT USER
            currentUser(e.getText().toString());
            int currency = AppCurrency.Companion.getGlobalCurrency();
            System.out.println(currency);

            int getBal = getUserBalance(e.getText().toString());
            currency = getBal;//redundent
            AppCurrency.Companion.setGlobalCurrency(currency);
            System.out.println(currency);
            startActivity(new Intent(this, PlayerActivity.class));
        } else {
            Toast.makeText(LoginActivity.this, "Incorrect Details", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Check the user input to see if the account exists and the password is correct.
     *
     * @param userEmail - the entered email address.
     * @param password  - the entered password.
     * @return - true if the account exists and the password is correct, false otherwise.
     */
    private boolean checkLoginCreds(String userEmail, String password) {
        String[] projection = {
                UserEntry.COLUMN_EMAIL_ADDRESS,
                UserEntry.COLUMN_PASSWORD, UserEntry.COLUMN_SALT};

        String selection = UserEntry.COLUMN_EMAIL_ADDRESS + " LIKE ? ";

        String[] selectionArgs = {userEmail};

        // Sorting the results.
        String sortOrder = UserEntry.COLUMN_EMAIL_ADDRESS + " DESC";

        try (Cursor cursor = mDatabase.query(
                UserEntry.TABLE_NAME,    // Table to query
                projection,                             // The array of columns to return
                selection,                              // The columns for the WHERE clause
                selectionArgs,                          // The values for the WHERE clause
                null,                                   // Don't group the rows
                null,                                   // Don't filter by the row groups
                sortOrder)) {                           // Order to sort

            String storedPassword = null;
            String storedSalt = null;

            // Query should give only 1 response - assuming emails are unique.
            while (cursor.moveToNext()) {
                storedPassword = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_PASSWORD));
                storedSalt = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_SALT));
            }
            // Account exists. Check that the passwords are the same.
            if (storedPassword != null) {
                try {
                    byte[] byteHash = PasswordUtilities.generateHash(password, storedSalt, "SHA-256");
                    String hash = PasswordUtilities.hexBytes(byteHash);
                    return hash.equals(storedPassword);
                }
                // Fail Condition. Keep the algorithm hard-coded.
                catch (NoSuchAlgorithmException exc) {
                    return false;
                }
            }
            // No account found.
            else {
                return false;
            }
        }
    }

    public static void currentUser(String email) {

        String currentUser = getCurrentUser();
        if (currentUser.equals(""))//no entry within the db | needs to be written to
        {
            ContentValues cV = new ContentValues();
            cV.put(CurrentUser.COLUMN_USER_EMAIL, email);
            mDatabase.insert(CurrentUser.TABLE_NAME, null, cV);
        } else {
            updateCurrentUser(email, currentUser);
        }
    }

    //check if the current logged in user is the within the current db
    public static String getCurrentUser() {// returns the current user within the db
        String[] projection = {
                CurrentUser.COLUMN_USER_EMAIL};

        String selection = CurrentUser.COLUMN_USER_CURRENT_ID + "  = ? ";

        String[] selectionArgs = {"1"};

        // Sorting the results.
        String sortOrder = CurrentUser.COLUMN_USER_EMAIL + " DESC";

        List itemsIds = new ArrayList<>();

        String currentUser = "";
        try (Cursor cursor = mDatabase.query(
                CurrentUser.TABLE_NAME,    // Table to query
                projection,                        // The array of columns to return
                selection,                         // The columns for the WHERE clause
                selectionArgs,                     // The values for the WHERE clause
                null,                                   // Don't group the rows
                null,                                   // Don't filter by the row groups
                sortOrder)) {                           // Order to sort

            while (cursor.moveToNext()) {
                String userStored = cursor.getString(cursor.getColumnIndex(CurrentUser.COLUMN_USER_EMAIL));
                currentUser = userStored;
            }

        } catch (Error e) {
            // TODO: Something here.
            System.out.println(e);
        }
        return currentUser;
    }

    private static void updateCurrentUser(String email, String currentUser) {
        if (!currentUser.equals(email))//not the current user | needs to be updated
        {
            String currentUserSelection = CurrentUser.COLUMN_USER_CURRENT_ID + "  = 1";

            //update
            ContentValues cV = new ContentValues();
            cV.put(CurrentUser.COLUMN_USER_EMAIL, email);

            mDatabase.update(CurrentUser.TABLE_NAME, cV, currentUserSelection, null);
        } else {
            //same user logging in
        }
    }

    private Integer getUserBalance(String email) {
        Integer balance = null;
        //get balance of current user from db
        //write to global currency variable

        //set this

        String[] projection = {
                UserEntry.COLUMN_USER_BALANCE};

        String selection = UserEntry.COLUMN_EMAIL_ADDRESS + " = ? ";

        String[] selectionArgs = {email};

        // Sorting the results.
        String sortOrder = UserEntry.COLUMN_EMAIL_ADDRESS + " DESC";

        try (Cursor cursor = mDatabase.query(
                UserEntry.TABLE_NAME,    // Table to query
                projection,                             // The array of columns to return
                selection,                              // The columns for the WHERE clause
                selectionArgs,                          // The values for the WHERE clause
                null,                                   // Don't group the rows
                null,                                   // Don't filter by the row groups
                sortOrder)) {                           // Order to sort

            // Query should give only 1 response - assuming emails are unique.
            while (cursor.moveToNext()) {
                balance = cursor.getInt(cursor.getColumnIndex(UserEntry.COLUMN_USER_BALANCE));

            }
            if (balance.equals(null)) {
                return 0;
            } else {
                return balance;
            }
        }
    }


}














