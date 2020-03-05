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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TeamRouteDetails {

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
    public void teamRouteDetails() {
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
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.goToTeamRoutes), withText("Team Routes"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.startingPoint), withText("Start: Garden Grove"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayoutInfo),
                                        childAtPosition(
                                                withId(R.id.infoLayout),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Start: Garden Grove")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.routeSteps), withText("Steps: 30"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayoutInfo),
                                        childAtPosition(
                                                withId(R.id.infoLayout),
                                                0)),
                                2),
                        isDisplayed()));
        textView2.check(matches(withText("Steps: 30")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.startingPoint), withText("Start: Archer Ave"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayoutInfo),
                                        childAtPosition(
                                                withId(R.id.infoLayout),
                                                0)),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("Start: Archer Ave")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.routeName), withText("Celt Drive"),
                        childAtPosition(
                                allOf(withId(R.id.iconLayout),
                                        childAtPosition(
                                                withId(R.id.parentLayout),
                                                0)),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("Celt Drive")));
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
