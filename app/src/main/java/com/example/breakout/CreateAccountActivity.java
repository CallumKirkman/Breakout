package com.example.breakout;

import android.os.Bundle;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.account_creation);
        setContentView(R.layout.activity_create_account);

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
        forename =  findViewById(R.id.enterForename);
        surname = findViewById(R.id.enterSurname);
        passwordInput = findViewById(R.id.enterPassword);
        emailAddressInput = findViewById(R.id.enterEmail);

        btnSubmit = findViewById(R.id.createAccountButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = forename.getText().toString();
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