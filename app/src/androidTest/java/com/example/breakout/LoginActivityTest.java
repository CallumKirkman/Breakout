package com.example.breakout;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    private LoginActivity loginAct = null;

    @Before
    public void setUp() throws Exception {
        loginAct = loginTestRule.getActivity();
    }


    /**
     * Ensure all components exist and are accessible in the Activity.
     */
    @Test
    public void testComponentsAvailable() {
        TextView title = loginAct.findViewById(R.id.Breakout);
        assertNotNull(title);

        EditText email = loginAct.findViewById(R.id.enterEmail);
        assertNotNull(email);

        EditText password = loginAct.findViewById(R.id.enterPassword);
        assertNotNull(password);

        TextView link = loginAct.findViewById(R.id.createAccountLink);
        assertNotNull(link);

        Button login = loginAct.findViewById(R.id.loginButton);
        assertNotNull(login);
    }


    @After
    public void tearDown() throws Exception {
        loginAct = null;
    }




    @Test
    public void onCreate() {
    }

    @Test
    public void onClickLogin() {
    }
}