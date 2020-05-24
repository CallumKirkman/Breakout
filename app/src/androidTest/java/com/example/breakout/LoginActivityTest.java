package com.example.breakout;

import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginTestRule = new ActivityTestRule<>(LoginActivity.class);


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
     * Test all Login fields accept input.
     */
    @Test
    public void testAcceptsCombinedInput() {
        onView(withId(R.id.enterEmail)).perform(typeText("example@email.com"));
        onView(withId(R.id.enterPassword)).perform(typeText("ExamplePassword123!"));
        onView(withId(R.id.loginButton)).perform(click());
    }


    /**
     * Test the email field accepts input.
     */
    @Test
    public void testInputEmail() {
        onView(withId(R.id.enterEmail)).perform(typeText("example@email.com"));
    }


    /**
     * Test the password field accepts input.
     */
    @Test
    public void testInputPassword() {
        onView(withId(R.id.enterPassword)).perform(typeText("ExamplePassword123!"));
    }

    /**
     * Test the login button accepts clicks.
     */
    @Test
    public void testLoginButton() {
        onView(withId(R.id.loginButton)).perform(click());
    }

}