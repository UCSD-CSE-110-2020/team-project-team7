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
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Espresso UI Tester that tests to see if the user inputs notes on the
 * notes page. Does various checks as well.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class NotesPageTest {
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
    public void notesPageTest() {
        mActivityTestRule.launchActivity(intent);
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.height_save_btn), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.routesButt), withText("ROUTES"),
                        childAtPosition(
                                allOf(withId(R.id.buttonLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                3)),
                                1),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.addRouteButton), withText("+"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.routeNameEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.routeNameStartingLayout),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("hello"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.startingPEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.routeNameStartingLayout),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("hello"), closeSoftKeyboard());


        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.notesButton), withText("Notes"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayoutButtons),
                                        childAtPosition(
                                                withId(R.id.parentLayout),
                                                5)),
                                0),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.notesEditText),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("hello world"), closeSoftKeyboard());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.SaveButton), withText("Save"),
                        childAtPosition(
                                allOf(withId(R.id.LinearLayout02),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                0),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.notesButton), withText("Notes"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayoutButtons),
                                        childAtPosition(
                                                withId(R.id.parentLayout),
                                                5)),
                                0),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.notesEditText), withText("hello world"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        editText.check(matches(withText("hello world")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
