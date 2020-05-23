package com.example.breakout;

import androidx.test.espresso.intent.rule.IntentsTestRule;
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


public class CreateAccountActivityTest {

    @Rule
    public IntentsTestRule<CreateAccountActivity> createTestRule = new IntentsTestRule<>(CreateAccountActivity.class);

    /**
     * Test all components are displayed in the Activity.
     */
    @Test
    public void testAllComponentsDisplayed() {
        onView(withId(R.id.enterForename)).check(matches(isDisplayed()));
        onView(withId(R.id.enterSurname)).check(matches(isDisplayed()));
        onView(withId(R.id.enterEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.enterPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.enterConfirmPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordHelpButton)).check(matches(isDisplayed()));
        onView(withId(R.id.termsLink)).check(matches(isDisplayed()));
        onView(withId(R.id.returnToLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.createAccountButton)).check(matches(isDisplayed()));
    }

    /**
     * Test all Create Account fields accept input.
     */
    @Test
    public void testAcceptsCombinedInput() {
        onView(withId(R.id.enterForename)).perform(typeText("Test Forename"));
        onView(withId(R.id.enterSurname)).perform(typeText("Test Surname"));
        onView(withId(R.id.enterEmail)).perform(typeText("example@email.com"));
        onView(withId(R.id.enterPassword)).perform(typeText("Password123!"));
        onView(withId(R.id.enterConfirmPassword)).perform(typeText("Password123!"));
    }


    /**
     * Test the forename field accepts input.
     */
    @Test
    public void testInputForename() {
        onView(withId(R.id.enterForename)).perform(typeText("Test Forename"));
    }


    /**
     * Test the surname field accepts input.
     */
    @Test
    public void testInputSurname() {
        onView(withId(R.id.enterSurname)).perform(typeText("Test Surname"));
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
        onView(withId(R.id.enterPassword)).perform(typeText("Test Password"));
    }


    /**
     * Test the confirm password field accepts input.
     */
    @Test
    public void testInputConfirmPassword() {
        onView(withId(R.id.enterConfirmPassword)).perform(typeText("Test Password"));
    }


    /**
     * Test that clicking the return button takes the user back to the Login activity.
     */
    @Test
    public void testReturnToLoginPage() {
        onView(withId(R.id.returnToLogin)).perform(click());
        intended(hasComponent(LoginActivity.class.getName()));
    }


    /**
     * Test that clicking the terms and conditions text displays a popup.
     */
    @Test
    public void testTermsClickable() {
        onView(withId(R.id.termsLink)).perform(click());
    }


    /**
     * Test that clicking the password help button display a popup.
     */
    @Test
    public void testPasswordHelpPopup() {
        onView(withId(R.id.passwordHelpButton)).perform(click());
    }


    /**
     * Test that the create account button reacts to onClick().
     */
    @Test
    public void testCreateAccountButton() {
        onView(withId(R.id.createAccountButton)).perform(click());
    }
}
