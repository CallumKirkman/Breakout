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


    private String forename, surname, password, confirmPassword, emailAddress; // Input Strings
    private String algorithm = "SHA-256";

    EditText forenameInput;
    EditText surnameInput;
    EditText passwordInput;
    EditText confirmPasswordInput;
    EditText emailAddressInput;
    Button btnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // TODO: Add popup for viewing password constraints.
        UserDBHelper dbHelper = new UserDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        
        mReadDatabase = dbHelper.getReadableDatabase();
        getUserInput();

    }


    public void onClickReturn(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void onClickTerms(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.popup_terms, null);


        // TODO: Different popup dimensions depending on screen orientation.
        int popupWidth = 600;
        int popupHeight = 850;
        final PopupWindow popupWindow = new PopupWindow(popupView, popupWidth, popupHeight, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        /*
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

         */
    }


    public void onClickPasswordHelp(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.popup_password_help, null);
        // TODO: Different popup dimensions depending on screen orientation.
        int popupWidth = 500;
        int popupHeight = 200;
        final PopupWindow popupWindow = new PopupWindow(popupView, popupWidth, popupHeight, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, Gravity.START, 0);

    }

    private boolean checkUserEmailExists(String userEmail)
    {
        // TODO: Check the password is correct against the stored password.

        String[] projection = {
                UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS};

        String selection = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS +
                " LIKE ? ";

        String[] selectionArgs = {userEmail};

        // Sorting the results.
        String sortOrder = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS + " DESC";

        List itemsIds = new ArrayList<>();

        try (Cursor cursor = mReadDatabase.query(
                UserDBContract.UserEntry.TABLE_NAME,    // Table to query
                projection,                        // The array of columns to return
                selection,                         // The columns for the WHERE clause
                selectionArgs,                     // The values for the WHERE clause
                null,                                   // Don't group the rows
                null,                                   // Don't filter by the row groups
                sortOrder)) {                           // Order to sort

            while (cursor.moveToNext()) {
                long itemID = cursor.getLong(cursor.getColumnIndexOrThrow(UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS));
                itemsIds.add(itemID);
            }
        }
        catch (Error e) {
            // TODO: Something here.
            System.out.println(e);
        }

        // If the table is larger than 0 the username is used within the db else it doesn't exist.
        // So its a new user without an account or inputted incorrectly.
        return itemsIds.size() > 0;
    }

    private void getUserInput() {
        forenameInput = (EditText) findViewById(R.id.enterForename);
        surnameInput = findViewById(R.id.enterSurname);
        passwordInput = (EditText) findViewById(R.id.enterPassword);
        confirmPasswordInput = findViewById(R.id.enterConfirmPassword);
        emailAddressInput = (EditText) findViewById(R.id.enterEmail);

        btnSubmit = findViewById(R.id.createAccountButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values.
                forename = forenameInput.getText().toString();
                surname = surnameInput.getText().toString();
                password = passwordInput.getText().toString();
                confirmPassword = confirmPasswordInput.getText().toString();
                emailAddress = emailAddressInput.getText().toString();

                // Validate input - Email, Password, and Names.
                if (!InputValidation.validateEmail(emailAddress)) {
                    Toast.makeText(CreateAccountActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
                else if (!InputValidation.validatePassword(password)) {
                    Toast.makeText(CreateAccountActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }
                else if(!InputValidation.validateName(forename) && !InputValidation.validateName(surname)) {
                    Toast.makeText(CreateAccountActivity.this, "Invalid Name", Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(confirmPassword)) {
                    Toast.makeText(CreateAccountActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
                // Salt and hash the password. Store the account.
                else {
                    if(!checkUserEmailExists(emailAddress)) {
                        try {
                            String salt = PasswordUtilities.generateSalt(4);
                            byte[] hashBytes = PasswordUtilities.generateHash(password, salt, algorithm);
                            String hash = PasswordUtilities.hexBytes(hashBytes);

                            ContentValues cV = new ContentValues();
                            cV.put(UserDBContract.UserEntry.COLUMN_FORENAME, forename);
                            cV.put(UserDBContract.UserEntry.COLUMN_SURNAME, surname);
                            cV.put(UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS, emailAddress);
                            cV.put(UserDBContract.UserEntry.COLUMN_PASSWORD, hash);
                            cV.put(UserDBContract.UserEntry.COLUMN_SALT, salt);

                            mDatabase.insert(UserDBContract.UserEntry.TABLE_NAME, null, cV);

                            startActivity(new Intent(CreateAccountActivity.this, PlayerActivity.class));
                        }

                        catch (NoSuchAlgorithmException exc) {
                            Toast.makeText(CreateAccountActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(CreateAccountActivity.this, "Email is associated with another account", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


    }

}