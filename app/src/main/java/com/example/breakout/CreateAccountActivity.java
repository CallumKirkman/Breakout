package com.example.breakout;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow;
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
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.account_creation);
        setContentView(R.layout.activity_create_account);

        UserDBHelper dbHelper = new UserDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        mRDatabase = dbHelper.getReadableDatabase();
        getUserInput();

    }


    public void onClickTerms(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View popupView = inflater.inflate(R.layout.popup_terms, null);


        // TODO: Difference popup dimensions depending on screen orientation.
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




    private void getUserInput() {
        forenameInput = (EditText) findViewById(R.id.enterForename);
        surnameInput = findViewById(R.id.enterSurname);
        passwordInput = (EditText) findViewById(R.id.enterPassword);
        emailAddressInput = (EditText) findViewById(R.id.enterEmail);

        btnSubmit = findViewById(R.id.createAccountButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forename = forenameInput.getText().toString();
                surname = surnameInput.getText().toString();
                password = passwordInput.getText().toString();
                emailAddress = emailAddressInput.getText().toString();

                //Check user input
                if (!InputValidation.validateEmail(emailAddress))
                {
                    //user need to enter valid email
                    Toast.makeText(CreateAccountActivity.this, "Email invalid", Toast.LENGTH_SHORT).show();
                }
                else if (!InputValidation.validatePassword(password))
                {
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
}