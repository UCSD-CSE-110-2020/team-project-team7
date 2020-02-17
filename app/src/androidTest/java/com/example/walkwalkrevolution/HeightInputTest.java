package com.example.walkwalkrevolution;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
/**
 * Espresso UI Tester that tests to see if the user inputs a height.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class HeightInputTest {
    Intent intent;
    private static final String TEST_SERVICE = "TEST_SERVICE";

    @Rule
    public ActivityTestRule<HomePage> mActivityTestRule = new ActivityTestRule<>(HomePage.class,true,false);

    @Before
    public void setUp(){
        clearSharedPrefs();
        intent = new Intent( getInstrumentation().getTargetContext(), HomePage.class);
        intent.putExtra(HomePage.FITNESS_SERVICE_KEY, TEST_SERVICE);
        intent.putExtra("testStep", true);
    }

    private void clearSharedPrefs() {
        Context targetContext = getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = targetContext.
                getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("my_first_time", true).commit();
    }


    @Test
    public void heightInputTest() {
        mActivityTestRule.launchActivity(intent);
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.height_input_ft), withText("7"),
                        isDisplayed()));
        appCompatEditText.perform(click());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.height_input_ft), withText("7"),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("5"));

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.height_input_ft), withText("5"),
                        isDisplayed()));
        appCompatEditText3.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.height_input_ft), withText("5"),
                        isDisplayed()));
        appCompatEditText4.perform(pressImeActionButton());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.height_input_in), withText("5"),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("6"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.height_input_in), withText("6"),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.height_input_in), withText("6"),
                        isDisplayed()));
        appCompatEditText7.perform(pressImeActionButton());

        ViewInteraction editText = onView(
                allOf(withId(R.id.height_input_ft), withText("5"),
                        isDisplayed()));
        editText.check(matches(withText("5")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.height_input_in), withText("6"),
                        isDisplayed()));
        editText2.check(matches(withText("6")));
    }

}
