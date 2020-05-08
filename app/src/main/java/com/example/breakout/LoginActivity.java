package com.example.breakout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView createAccountLink = findViewById(R.id.createAccountLink);

        createAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch CreateAccountActivity from text link.
                Intent createAccount = new Intent(getBaseContext(), CreateAccountActivity.class);
                startActivity(createAccount);
            }
        });
    }

    public void onClickLogin(View view) {
        // Launch the app.
        Intent login = new Intent(this, SpotifyConnect.class);
        startActivity(login);
    }
}
