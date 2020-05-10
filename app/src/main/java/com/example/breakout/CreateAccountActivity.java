package com.example.breakout;

import android.os.Bundle;

import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.account_res.InputValidation;

import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {

    private String name, password, emailAddress;

    EditText forename;
    EditText surname;
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
        forename = (EditText) findViewById(R.id.enterForename);
        surname = findViewById(R.id.enterSurname);
        passwordInput = (EditText) findViewById(R.id.enterPassword);
        emailAddressInput = (EditText) findViewById(R.id.enterEmail);

        btnSubmit = (Button) findViewById(R.id.createAccountButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = forename.getText().toString();
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
                    Toast.makeText(CreateAccountActivity.this, "invalid ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                 //Continue to next page
                    Boolean pass = true;
                }

            }
        });
    }
}