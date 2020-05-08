package com.example.breakout;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    private String name, password, emailAddress;

    EditText uNameInput;
    EditText passwordInput;
    EditText emailAddressInput;

    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getUserInput();
    }

    private void getUserInput() {
        uNameInput = (EditText) findViewById(R.id.uNameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        emailAddressInput = (EditText) findViewById(R.id.emailAdressInput);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = uNameInput.getText().toString();
                password = passwordInput.getText().toString();
                emailAddress = emailAddressInput.getText().toString();


            }
        });
    }

}