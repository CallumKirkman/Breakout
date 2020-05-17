package com.example.breakout;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import android.service.autofill.UserData;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.account_res.InputValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private SQLiteDatabase mRDatabase;

    private String forename, surname, password, emailAddress;

    EditText forenameInput;
    EditText surnameInput;
    EditText passwordInput;
    EditText emailAddressInput;

    Button btnSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create Account");
        setContentView(R.layout.activity_create_account);

        UserDBHelper dbHelper = new UserDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        mRDatabase = dbHelper.getReadableDatabase();

        getUserInput();
    }

    private void getUserInput() {
        forenameInput = (EditText) findViewById(R.id.enterForename);
        surnameInput = findViewById(R.id.enterSurname);
        passwordInput = (EditText) findViewById(R.id.enterPassword);
        emailAddressInput = (EditText) findViewById(R.id.enterEmail);

        btnSubmit = (Button) findViewById(R.id.createAccountButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forename = forenameInput.getText().toString();
                surname = surnameInput.getText().toString();
                password = passwordInput.getText().toString();
                emailAddress = emailAddressInput.getText().toString();

                //Check user input
                //if it's false the input is incorrect
                if (InputValidation.validateName(forename) == false) {
                    Toast.makeText(CreateAccountActivity.this, "name invalid", Toast.LENGTH_SHORT).show();

                } else if (InputValidation.validateEmail(emailAddress) == false) {
                    //user need to enter valid email
                    Toast.makeText(CreateAccountActivity.this, "Email invalid", Toast.LENGTH_SHORT).show();
                } else if (InputValidation.validatePassword(password) == false) {
                    //user need to enter valid password to meet requirements
                    Toast.makeText(CreateAccountActivity.this, "password invalid ", Toast.LENGTH_SHORT).show();
                } else {
                    // write data to database
                    // Continue to next page


                    ContentValues cV = new ContentValues();
                    cV.put(UserDBContract.UserEntry.COLUMN_FORENAME, forename);
                    cV.put(UserDBContract.UserEntry.COLUMN_SURNAME, surname);
                    cV.put(UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS, emailAddress);
                    cV.put(UserDBContract.UserEntry.COLUMN_PASSWORD, password);
                    cV.put(UserDBContract.UserEntry.COLUMN_SALT, 1234);


                    mDatabase.insert(UserDBContract.UserEntry.TABLE_NAME, null, cV);

                }


            }
        });
    }


//    public boolean usernameExists(String username) {
//
//        String[] projection = {
//                UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS
//        };
////
//        String selection = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS + " = ?";
//        String[] selectionArgs = {username};
//
//        //sorting the results
//        String sortOrder = UserDBContract.UserEntry.COLUMN_EMAIL_ADDRESS + " DESC";
//
//        try {
//           Cursor cursor = mRDatabase.query(
//                    UserDBContract.UserEntry.TABLE_NAME,    // Table to query
//                    projection,                             // The array of columns to return
//                    selection,                              // The columns for the WHERE clause
//                    selectionArgs,                          // The values for the WHERE clause
//                    null,                          // Don't group the rows
//                    null,                           // Don't filter by teh row groups
//                    sortOrder                              // Order to sort
//            );
//            List itemsIds = new ArrayList<>();
//            while (cursor.moveToNext()) {
//                long itemID = cursor.getLong(cursor.getColumnIndexOrThrow(UserDBContract.UserEntry.COLUMN_ID));
//                itemsIds.add(itemID);
//            }
//        } catch (Error e) {
//            System.out.println(e);
//        }
//        return false;
//    };



}