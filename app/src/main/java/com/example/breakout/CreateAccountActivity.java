package com.example.breakout;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.example.account_res.InputValidation;
import com.example.account_res.PasswordUtilities;


public class CreateAccountActivity extends Activity {

    private SQLiteDatabase mDatabase;
    private SQLiteDatabase mReadDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        UserDBHelper dbHelper = new UserDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        mReadDatabase = dbHelper.getReadableDatabase();

        final EditText password = findViewById(R.id.enterPassword);

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onClickPasswordHelp(v);
            }
        });
    }


    /**
     * Return to the login page.
     * @param view - view.
     */
    public void onClickReturn(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }


    /**
     * Display the terms and conditions in a popup window.
     * @param view - view.
     */
    public void onClickTerms(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.popup_terms, null);

        int popupWidth = 600;
        int popupHeight = 850;

        final PopupWindow popupWindow = new PopupWindow(popupView, popupWidth, popupHeight, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button closePopup = popupView.findViewById(R.id.closeTermsButton);
        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    /**
     * Display the password constraints in a popup window.
     * @param view - view.
     */
    public void onClickPasswordHelp(View view) {
        Button lock = findViewById(R.id.passwordHelpButton);
        lock.setBackgroundResource(R.drawable.ic_lock_outline);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.popup_password_help, null);

        int popupWidth = 500;
        int popupHeight = 200;
        final PopupWindow popupWindow = new PopupWindow(popupView, popupWidth, popupHeight, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, -80);

    }


    /**
     * See if the supplied string is an email in the database.
     * @param userEmail - the email to lookup.
     * @return - true if it exists, false otherwise.
     */
    private boolean checkUserEmailExists(String userEmail) {
        // Query Building - specify column, condition, sort order, etc.
        String[] projection = { UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS };
        String selection = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS + " LIKE ? ";
        String[] selectionArgs = { userEmail };
        String sortOrder = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS + " DESC";

        List<Long> itemsIds = new ArrayList<>();

        try (Cursor cursor = mReadDatabase.query(
                UserDBContract.UserEntry.TABLE_NAME,    // Table to query
                projection,                             // The array of columns to return
                selection,                              // The columns for the WHERE clause
                selectionArgs,                          // The values for the WHERE clause
                null,                                   // Don't group the rows
                null,                                   // Don't filter by the row groups
                sortOrder)) {                           // Order to sort

            while (cursor.moveToNext()) {
                long itemID = cursor.getLong(cursor.getColumnIndex(UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS));
                itemsIds.add(itemID);
            }
        }
        // Greater than 0, the email exists - true.
        return itemsIds.size() > 0;
    }


    /**
     * Get the user input. See if an account already exists.
     * If it doesn't, create the account. Load the song player.
     * @param view - view.
     */
    public void onClickCreateAccount(View view) {
        EditText forenameField = findViewById(R.id.enterForename);
        EditText surnameField = findViewById(R.id.enterSurname);
        EditText passwordField = findViewById(R.id.enterPassword);
        EditText confirmPasswordField = findViewById(R.id.enterConfirmPassword);
        EditText emailAddressField = findViewById(R.id.enterEmail);

        String forename = forenameField.getText().toString();
        String surname = surnameField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();
        String emailAddress = emailAddressField.getText().toString();

        // Validate input - email, password, and names.
        if (!InputValidation.validateEmail(emailAddress)) {
            Toast.makeText(CreateAccountActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
        }
        else if (!InputValidation.validatePassword(password)) {
            Toast.makeText(CreateAccountActivity.this, "Password does not meet constraints", Toast.LENGTH_SHORT).show();
        }
        else if(!InputValidation.validateName(forename) && !InputValidation.validateName(surname)) {
            Toast.makeText(CreateAccountActivity.this, "Invalid Name", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmPassword)) {
            Toast.makeText(CreateAccountActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
        // Account is valid. Salt and hash the password. Store the account.
        else {
            if(!checkUserEmailExists(emailAddress)) {
                try {
                    String salt = PasswordUtilities.generateSalt(4);
                    byte[] hashBytes = PasswordUtilities.generateHash(password, salt, "SHA-256");
                    String hash = PasswordUtilities.hexBytes(hashBytes);

                    ContentValues cV = new ContentValues();
                    cV.put(UserDBContract.UserEntry.COLUMN_FORENAME, forename);
                    cV.put(UserDBContract.UserEntry.COLUMN_SURNAME, surname);
                    cV.put(UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS, emailAddress);
                    cV.put(UserDBContract.UserEntry.COLUMN_PASSWORD, hash);
                    cV.put(UserDBContract.UserEntry.COLUMN_SALT, salt);

                    mDatabase.insert(UserDBContract.UserEntry.TABLE_NAME, null, cV);

                    // Launch the player.
                    startActivity(new Intent(CreateAccountActivity.this, PlayerActivity.class));
                }
                catch (NoSuchAlgorithmException exc) {
                    Toast.makeText(CreateAccountActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(CreateAccountActivity.this, "Email is associated with another account.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
