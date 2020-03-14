package com.example.walkwalkrevolution;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AcceptedTeamInvite {

    Intent intent;
    private static final String TEST_SERVICE = "TEST_SERVICE";

    @Rule
    public ActivityTestRule<HomePage> mActivityTestRule = new ActivityTestRule<>(HomePage.class,true,false);

    @Before
    public void setUp(){
        //clearSharedPrefs();
        intent = new Intent( getInstrumentation().getTargetContext(), HomePage.class);
        intent.putExtra(HomePage.FITNESS_SERVICE_KEY, TEST_SERVICE);
        intent.putExtra("testService", true);
        intent.putExtra("creator", true);
        intent.putExtra("testStep", true);
    }

    private void clearSharedPrefs() {
        Context targetContext = getInstrumentation().getTargetContext();
        SharedPreferences sharedPreferences = targetContext.
                getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("my_first_time", true).commit();
    }

    @Test
    public void homePageOriginal() {
        mActivityTestRule.launchActivity(intent);
    }
}
