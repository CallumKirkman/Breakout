package com.example.breakout;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.account_res.InputValidation;

import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {

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
                if (InputValidation.validateEmail(emailAddress) == false)
                {
                    //user need to enter valid email
                    Toast.makeText(CreateAccountActivity.this, "Email invalid", Toast.LENGTH_SHORT).show();
                }
                else if (InputValidation.validatePassword(password) == false)
                {
                    //user need to enter valid password to meet requirements
                    Toast.makeText(CreateAccountActivity.this, "password invalid ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // write data to database
                    // Continue to next page


                   


                }


            }
        });
    }
}