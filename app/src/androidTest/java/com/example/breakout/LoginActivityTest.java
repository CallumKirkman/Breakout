package com.example.breakout;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginTestRule = new ActivityTestRule<>(LoginActivity.class);

    private LoginActivity loginAct = null;

    @Before
    public void setUp() throws Exception {
        loginAct = loginTestRule.getActivity();
    }


    /**
     * Ensure all components exist and are accessible in the Activity.
     */
    @Test
    public void testAllComponentsDisplayed() {
        onView(withId(R.id.Breakout)).check(matches(isDisplayed()));
        onView(withId(R.id.enterEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.enterPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.createAccountLink)).check(matches(isDisplayed()));
    }


    /**
     * Run this one.
     */
    @Test
    public void testComponentsAcceptInput() {
        onView(withId(R.id.enterEmail)).perform(typeText("example@email.com"));
        onView(withId(R.id.enterPassword)).perform(typeText("ExamplePassword123!"));
        onView(withId(R.id.loginButton)).perform(click());
    }


    @Test
    public void testEmailAcceptsInput() {
        onView(withId(R.id.enterEmail)).perform(typeText("example@email.com"));
    }

    @Test
    public void testPasswordAcceptsInput() {
        onView(withId(R.id.enterPassword)).perform(typeText("ExamplePassword123!"));
    }



    @Test
    public void testButtonRejectsEmptyData() {





    }

    /**
     * Supply a valid, created account. See if an Intent is launched.
     */
    @Test
    public void testButtonAcceptsValidData() {
        onView(withId(R.id.enterPassword)).perform(typeText("ExamplePassword123!"));
        onView(withId(R.id.enterEmail)).perform(typeText("example@email.com"));
        onView(withId(R.id.loginButton)).perform(click());

        intended(hasComponent(PlayerActivity.class.getName()));
    }



}