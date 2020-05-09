package com.example.breakout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.example.account_res.*;


public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView createAccountLink = findViewById(R.id.createAccountLink);

        createAccountLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch CreateAccountActivity from text link.
                startActivity(new Intent(getBaseContext(), CreateAccountActivity.class));
            }
        });
    }




    public void onClickLogin(View view) {
        // Get email, password.
        EditText email = findViewById(R.id.enterEmail);

        String testEmail = email.getText().toString();

        // Look up email in DB.

        // If it doesn't exist, display error message.

        // Else, get stored password, and salt.

        // Salt and then hash the entered password.

        // Compare the two passwords. If equal, login.

        Intent login = new Intent(this, SpotifyConnect.class);
        startActivity(login);
    }
}
