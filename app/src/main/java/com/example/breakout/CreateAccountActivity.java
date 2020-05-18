package com.example.breakout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import com.example.account_res.InputValidation;
import com.example.account_res.PasswordUtilities;


public class CreateAccountActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;


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
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.account_creation);
        setContentView(R.layout.activity_create_account);

        // TODO: Add popup for viewing password constraints.
        UserDBHelper dbHelper = new UserDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        getUserInput();

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

    private void checkUserEmailExists(String userEmail)
    {



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
                    catch(NoSuchAlgorithmException exc) {
                        Toast.makeText(CreateAccountActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

}